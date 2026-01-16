import React, { useState } from 'react';
import { Vehicle } from '@/types/vehicle';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { ShoppingCart, Zap, Fuel, Gauge, Battery, Play, Pause, Car, Tag, Bike } from 'lucide-react';
import { useCart } from '@/context/CartContext';
import { useStock } from '@/context/StockContext';
import { cn } from '@/lib/utils';
import { motion } from 'framer-motion';

interface VehicleCardProps {
  vehicle: Vehicle;
  compact?: boolean;
}

const VehicleCard: React.FC<VehicleCardProps> = ({ vehicle, compact = false }) => {
  const { addToCart } = useCart();
  const { isSoldered, getDiscount } = useStock();
  const [isAnimating, setIsAnimating] = useState(false);
  const [isHovered, setIsHovered] = useState(false);

  if (!vehicle) return null;

  // --- GESTION DE L'IMAGE DU BACKEND (Correction imageLink) ---
  const API_BASE_URL = "http://10.49.20.91:8000";
  
  const getImageUrl = () => {
 
    const data = vehicle.imageLink || vehicle.imageData;

    if (!data || data === "") return null;

    // Si c'est déjà une URL complète ou du Base64
    if (typeof data === 'string' && (data.startsWith('data:') || data.startsWith('http'))) {
      return data;
    }

    // Si c'est un chemin type /uploads/vehicules/vehicule-5.jpg
    const cleanPath = data.startsWith('/') ? data : `/${data}`;
    return `${API_BASE_URL}${cleanPath}`;
  };

  const imageUrl = getImageUrl();

  // --- LOGIQUE MÉTIER ---
  const vId = vehicle.id ? String(vehicle.id) : "";
  const vehicleIsSoldered = isSoldered(vId);
  const discountPercent = getDiscount(vId);

  // Utilisation de description ou typeVehicule pour déterminer l'énergie
  const typeStr = (vehicle.typeVehicule || vehicle.description || "").toUpperCase();
  const isElectric = typeStr.includes('ELECTRIQUE') || typeStr.includes('ÉLECTRIQUE');
  const isScooter = typeStr.includes('SCOOTER');
  
  const basePrice = vehicle.prixBase ?? 0;
  const finalPrice = vehicleIsSoldered 
    ? basePrice * (1 - discountPercent / 100)
    : basePrice;

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency', currency: 'EUR', maximumFractionDigits: 0
    }).format(price);
  };

  const toggleAnimation = (e: React.MouseEvent) => {
    e.stopPropagation();
    setIsAnimating(!isAnimating);
  };

  return (
    <motion.div
      className={cn(
        "group relative overflow-hidden rounded-2xl bg-[#0a0a0a] border border-white/5 hover:border-primary/50 transition-all duration-500",
        compact ? "flex flex-row" : ""
      )}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => { setIsHovered(false); setIsAnimating(false); }}
      whileHover={{ y: -8 }}
    >
      {/* Badges Dynamiques */}
      <div className="absolute top-4 left-4 z-10 flex flex-col gap-2">
        {vehicleIsSoldered && (
          <Badge className="bg-red-500/10 text-red-500 border-red-500/20 font-bold px-3 py-1 rounded-full text-xs backdrop-blur-md">
            <Tag className="w-3 h-3 mr-1" /> -{discountPercent}%
          </Badge>
        )}
        {isElectric && (
          <Badge className="bg-emerald-500/10 text-emerald-500 border-emerald-500/20 font-medium px-3 py-1 rounded-full text-xs backdrop-blur-md">
            <Zap className="w-3 h-3 mr-1" /> Électrique
          </Badge>
        )}
      </div>

      <motion.button
        onClick={toggleAnimation}
        className={cn("absolute top-4 right-4 z-10 w-10 h-10 rounded-full flex items-center justify-center bg-primary/90 text-primary-foreground backdrop-blur-sm shadow-xl")}
        initial={{ opacity: 0 }}
        animate={{ opacity: isHovered ? 1 : 0 }}
      >
        {isAnimating ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4 ml-0.5" />}
      </motion.button>

      {/* Visuel du véhicule */}
      <div className={cn("relative overflow-hidden bg-gradient-to-br from-secondary/50 to-muted/30", compact ? "w-56 h-36 flex-shrink-0" : "aspect-[16/10]")}>
        <div className="absolute inset-0">
          {imageUrl ? (
            <motion.img
              src={imageUrl}
              alt={`${vehicle.marque} ${vehicle.modele}`}
              className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
              animate={isAnimating ? { x: [0, 3, -3, 0] } : {}}
              onError={(e) => {
                (e.target as HTMLImageElement).style.display = 'none';
              }}
            />
          ) : (
            <div className="flex items-center justify-center h-full">
              <motion.div animate={isAnimating ? { x: [0, 10, -10, 0] } : {}} className="text-center">
                 {isScooter ? (
                   <Bike className={cn("w-20 h-20 mx-auto transition-all duration-700", isAnimating ? "text-primary scale-110" : "text-white/10")} />
                 ) : (
                   <Car className={cn("w-20 h-20 mx-auto transition-all duration-700", isAnimating ? "text-primary scale-110" : "text-white/10")} />
                 )}
              </motion.div>
            </div>
          )}
          <div className="absolute inset-0 bg-gradient-to-t from-[#0a0a0a] via-transparent to-transparent opacity-40" />
        </div>
      </div>

      {/* Détails du véhicule */}
      <div className={cn("p-6", compact ? "flex-1 flex flex-col justify-between" : "")}>
        <div className="mb-4">
          <p className="text-[10px] text-primary font-bold uppercase tracking-[0.2em] mb-1">{vehicle.marque}</p>
          <h3 className="text-xl font-display font-semibold text-white tracking-tight">{vehicle.modele}</h3>
        </div>

        <div className="flex gap-4 mb-6">
          <div className="flex items-center gap-2 text-xs text-white/40">
            <Gauge className="w-3.5 h-3.5" /> 
            <span>{vehicle.cylindree && vehicle.cylindree > 0 ? `${vehicle.cylindree} cc` : 'N/A'}</span>
          </div>
          <div className="flex items-center gap-2 text-xs text-white/40">
            {isElectric ? <Battery className="w-3.5 h-3.5" /> : <Fuel className="w-3.5 h-3.5" />}
            <span>{isElectric ? 'Électrique' : 'Essence'}</span>
          </div>
        </div>

        <div className="flex items-center justify-between mt-auto">
          <div className="flex flex-col">
            {vehicleIsSoldered && (
              <span className="text-xs text-white/30 line-through mb-1">
                {formatPrice(basePrice)}
              </span>
            )}
            <span className={cn(
              "text-2xl font-display font-semibold tracking-tight",
              vehicleIsSoldered ? "text-red-400" : "text-white"
            )}>
              {formatPrice(finalPrice)}
            </span>
          </div>
          
          <Button 
            variant={vehicleIsSoldered ? "destructive" : "hero"} 
            size="sm" 
            className="rounded-full h-10 w-10 p-0"
            onClick={() => addToCart(vehicle)}
          >
            <ShoppingCart className="w-4 h-4" />
          </Button>
        </div>
        <p className="text-[10px] text-white/10 font-mono mt-4 uppercase tracking-tighter">REF: {vehicle.reference || 'N/A'}</p>
      </div>
    </motion.div>
  );
};

export default VehicleCard;