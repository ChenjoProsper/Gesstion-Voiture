import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '@/components/layout/Navbar';
import Footer from '@/components/layout/Footer';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { useAuth } from '@/context/AuthContext';
import { useCart } from '@/context/CartContext';
import { useStock } from '@/context/StockContext';
import { vehicles, sampleSubsidiaries } from '@/data/vehicles';
import { Subsidiary } from '@/types/vehicle';
import { 
  Building2, 
  AlertTriangle, 
  Car, 
  Users, 
  ChevronRight, 
  ChevronDown,
  ShoppingCart,
  MapPin,
  Plus
} from 'lucide-react';
import { cn } from '@/lib/utils';

interface SubsidiaryNodeProps {
  subsidiary: Subsidiary;
  level: number;
}

const SubsidiaryNode: React.FC<SubsidiaryNodeProps> = ({ subsidiary, level }) => {
  const [isExpanded, setIsExpanded] = useState(level < 2);
  const hasChildren = subsidiary.children.length > 0;

  return (
    <div className="space-y-2">
      <div 
        className={cn(
          "flex items-center gap-3 p-3 rounded-lg border transition-all cursor-pointer",
          level === 0 ? "border-primary bg-primary/10" : "border-border bg-card hover:border-primary/50"
        )}
        style={{ marginLeft: `${level * 24}px` }}
        onClick={() => hasChildren && setIsExpanded(!isExpanded)}
      >
        {hasChildren ? (
          isExpanded ? (
            <ChevronDown className="w-4 h-4 text-primary" />
          ) : (
            <ChevronRight className="w-4 h-4 text-muted-foreground" />
          )
        ) : (
          <div className="w-4" />
        )}
        <Building2 className={cn(
          "w-5 h-5",
          level === 0 ? "text-primary" : "text-muted-foreground"
        )} />
        <div className="flex-1">
          <p className="font-medium text-foreground">{subsidiary.name}</p>
          <p className="text-xs text-muted-foreground flex items-center gap-1">
            <MapPin className="w-3 h-3" />
            {subsidiary.location}
          </p>
        </div>
        {level === 0 && (
          <Badge variant="outline" className="border-primary text-primary">
            Siège
          </Badge>
        )}
      </div>
      
      {isExpanded && hasChildren && (
        <div className="space-y-2">
          {subsidiary.children.map(child => (
            <SubsidiaryNode key={child.id} subsidiary={child} level={level + 1} />
          ))}
        </div>
      )}
    </div>
  );
};

const Societe: React.FC = () => {
  const { isSociete, user } = useAuth();
  const { addToCart } = useCart();
  const { isSoldered, getDiscount } = useStock();
  const [selectedVehicles, setSelectedVehicles] = useState<string[]>([]);

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR',
      maximumFractionDigits: 0
    }).format(price);
  };

  const toggleVehicleSelection = (vehicleId: string) => {
    setSelectedVehicles(prev => 
      prev.includes(vehicleId) 
        ? prev.filter(id => id !== vehicleId)
        : [...prev, vehicleId]
    );
  };

  const addFleetToCart = () => {
    selectedVehicles.forEach(vehicleId => {
      const vehicle = vehicles.find(v => v.id === vehicleId);
      if (vehicle) {
        addToCart(vehicle);
      }
    });
    setSelectedVehicles([]);
  };

  const calculateFleetTotal = () => {
    return selectedVehicles.reduce((total, vehicleId) => {
      const vehicle = vehicles.find(v => v.id === vehicleId);
      if (!vehicle) return total;
      const discount = isSoldered(vehicleId) ? getDiscount(vehicleId) : 0;
      return total + vehicle.price * (1 - discount / 100);
    }, 0);
  };

  if (!isSociete) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <main className="pt-32 pb-24">
          <div className="container px-4 text-center">
            <AlertTriangle className="w-16 h-16 text-destructive mx-auto mb-6" />
            <h1 className="text-3xl font-display font-bold text-foreground mb-4">
              Accès réservé
            </h1>
            <p className="text-muted-foreground mb-4">
              Cette section est réservée aux comptes Société.
            </p>
            <p className="text-sm text-muted-foreground">
              Créez un compte en choisissant le type "Société" pour accéder à cet espace.
            </p>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="pt-32 pb-24">
        <div className="container px-4">
          {/* Header */}
          <div className="mb-12">
            <div className="flex items-center gap-3 mb-4">
              <div className="w-12 h-12 rounded-xl bg-primary/20 flex items-center justify-center">
                <Building2 className="w-6 h-6 text-primary" />
              </div>
              <div>
                <p className="text-primary font-medium">Espace Société</p>
                <h1 className="text-3xl font-display font-bold text-foreground">
                  {user?.company || 'Ma Société'}
                </h1>
              </div>
            </div>
            <p className="text-muted-foreground">
              Gérez votre flotte de véhicules et vos filiales.
            </p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Fleet Purchase Section */}
            <div className="lg:col-span-2 space-y-6">
              <div className="bg-card rounded-2xl border border-border p-6">
                <div className="flex items-center justify-between mb-6">
                  <div className="flex items-center gap-3">
                    <Car className="w-6 h-6 text-primary" />
                    <h2 className="text-xl font-display font-bold text-foreground">
                      Achat de Flotte
                    </h2>
                  </div>
                  {selectedVehicles.length > 0 && (
                    <Badge className="bg-gradient-violet">
                      {selectedVehicles.length} sélectionné(s)
                    </Badge>
                  )}
                </div>

                <p className="text-muted-foreground mb-6">
                  Sélectionnez plusieurs véhicules pour un achat groupé.
                </p>

                <div className="grid gap-4">
                  {vehicles.map(vehicle => {
                    const isSelected = selectedVehicles.includes(vehicle.id);
                    const vehicleIsSoldered = isSoldered(vehicle.id);
                    const discount = getDiscount(vehicle.id);
                    const finalPrice = vehicleIsSoldered 
                      ? vehicle.price * (1 - discount / 100) 
                      : vehicle.price;

                    return (
                      <div
                        key={vehicle.id}
                        className={cn(
                          "flex items-center justify-between p-4 rounded-xl border cursor-pointer transition-all",
                          isSelected 
                            ? "border-primary bg-primary/10" 
                            : "border-border bg-muted/50 hover:border-primary/50"
                        )}
                        onClick={() => toggleVehicleSelection(vehicle.id)}
                      >
                        <div className="flex items-center gap-4">
                          <div className={cn(
                            "w-6 h-6 rounded-full border-2 flex items-center justify-center transition-all",
                            isSelected 
                              ? "border-primary bg-primary" 
                              : "border-muted-foreground"
                          )}>
                            {isSelected && (
                              <div className="w-2 h-2 rounded-full bg-white" />
                            )}
                          </div>
                          <div className="w-12 h-10 rounded-lg bg-muted flex items-center justify-center">
                            <Car className="w-5 h-5 text-primary/50" />
                          </div>
                          <div>
                            <div className="flex items-center gap-2">
                              <h3 className="font-medium text-foreground">
                                {vehicle.brand} {vehicle.model}
                              </h3>
                              {vehicleIsSoldered && (
                                <Badge className="bg-gradient-violet text-xs">
                                  -{discount}%
                                </Badge>
                              )}
                            </div>
                            <p className="text-sm text-muted-foreground">
                              {vehicle.type === 'automobile' ? 'Automobile' : 'Scooter'} • {vehicle.stock} en stock
                            </p>
                          </div>
                        </div>
                        <div className="text-right">
                          {vehicleIsSoldered && (
                            <p className="text-xs text-muted-foreground line-through">
                              {formatPrice(vehicle.price)}
                            </p>
                          )}
                          <p className="font-bold text-gradient">
                            {formatPrice(finalPrice)}
                          </p>
                        </div>
                      </div>
                    );
                  })}
                </div>

                {/* Fleet Summary */}
                {selectedVehicles.length > 0 && (
                  <div className="mt-6 pt-6 border-t border-border">
                    <div className="flex items-center justify-between mb-4">
                      <div>
                        <p className="text-sm text-muted-foreground">Total de la flotte</p>
                        <p className="text-2xl font-bold text-gradient">
                          {formatPrice(calculateFleetTotal())}
                        </p>
                      </div>
                      <Button variant="hero" onClick={addFleetToCart}>
                        <ShoppingCart className="w-4 h-4 mr-2" />
                        Ajouter la flotte au panier
                      </Button>
                    </div>
                  </div>
                )}
              </div>
            </div>

            {/* Subsidiaries Section */}
            <div className="lg:col-span-1">
              <div className="bg-card rounded-2xl border border-border p-6 sticky top-28">
                <div className="flex items-center gap-3 mb-6">
                  <Users className="w-6 h-6 text-primary" />
                  <h2 className="text-xl font-display font-bold text-foreground">
                    Mes Filiales
                  </h2>
                </div>

                <p className="text-sm text-muted-foreground mb-6">
                  Structure hiérarchique de votre société (Pattern Composite).
                </p>

                <div className="space-y-2">
                  {sampleSubsidiaries.map(subsidiary => (
                    <SubsidiaryNode 
                      key={subsidiary.id} 
                      subsidiary={subsidiary} 
                      level={0} 
                    />
                  ))}
                </div>

                <Button variant="outline" className="w-full mt-6">
                  <Plus className="w-4 h-4 mr-2" />
                  Ajouter une filiale
                </Button>
              </div>
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default Societe;
