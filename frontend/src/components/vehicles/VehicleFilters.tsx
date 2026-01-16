import React from 'react';
import { Search, LayoutGrid, List, Car, Bike, Zap, Fuel } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { cn } from '@/lib/utils';

interface VehicleFiltersProps {
  search: string;
  onSearchChange: (value: string) => void;
  vehicleType: 'all' | 'automobile' | 'scooter';
  onVehicleTypeChange: (type: 'all' | 'automobile' | 'scooter') => void;
  fuelType: 'all' | 'essence' | 'electrique';
  onFuelTypeChange: (type: 'all' | 'essence' | 'electrique') => void;
  gridView: 1 | 3;
  onGridViewChange: (view: 1 | 3) => void;
  searchOperator: 'ET' | 'OU';
  onSearchOperatorChange: (operator: 'ET' | 'OU') => void;
}

const VehicleFilters: React.FC<VehicleFiltersProps> = ({
  search,
  onSearchChange,
  vehicleType,
  onVehicleTypeChange,
  fuelType,
  onFuelTypeChange,
  gridView,
  onGridViewChange,
  searchOperator,
  onSearchOperatorChange
}) => {
  // Style des labels conservé selon ton design
  const labelStyle = "text-xs font-medium text-muted-foreground uppercase tracking-widest block mb-3";

  return (
    <div className="space-y-8 p-8 bg-[#141417] rounded-[2rem] border border-white/[0.05] shadow-2xl">
      
      {/* Search - Texte : Recherche */}
      <div className="space-y-3">
        <label className={labelStyle}>Recherche</label>
        <div className="relative group">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground group-focus-within:text-primary transition-colors" />
          <Input
            value={search}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder="Marque, modèle..."
            className="pl-11 bg-[#0a0a0c] border-white/5 text-white placeholder:text-white/20 rounded-xl h-12 focus:ring-0 transition-all"
          />
        </div>
        <div className="flex gap-1.5 p-1 bg-[#0a0a0c] rounded-lg border border-white/5">
          {['ET', 'OU'].map((op) => (
            <button
              key={op}
              onClick={() => onSearchOperatorChange(op as 'ET' | 'OU')}
              className={cn(
                "flex-1 py-2 rounded-md text-xs font-medium transition-all duration-200",
                searchOperator === op 
                  ? "bg-[#d1d1c7] text-[#1a1a1e] shadow-sm" 
                  : "text-muted-foreground hover:text-white"
              )}
            >
              {op}
            </button>
          ))}
        </div>
      </div>

      {/* Vehicle Type - Texte : Type, Tous, Auto, Scooter */}
      <div className="space-y-3">
        <label className={labelStyle}>Type</label>
        <div className="flex flex-wrap gap-2">
          {[
            { value: 'all' as const, label: 'Tous', icon: null },
            { value: 'automobile' as const, label: 'Auto', icon: Car },
            { value: 'scooter' as const, label: 'Scooter', icon: Bike },
          ].map((item) => (
            <button
              key={item.value}
              onClick={() => onVehicleTypeChange(item.value)}
              className={cn(
                "flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-medium transition-all border",
                vehicleType === item.value
                  ? "bg-primary text-white border-primary"
                  : "bg-[#0a0a0c] border-white/5 text-muted-foreground hover:border-white/20 hover:text-white"
              )}
            >
              {item.icon && <item.icon className="w-3.5 h-3.5" />}
              {item.label}
            </button>
          ))}
        </div>
      </div>

      {/* Fuel Type - Texte : Énergie, Tous, Essence, Électrique */}
      <div className="space-y-3">
        <label className={labelStyle}>Énergie</label>
        <div className="flex flex-wrap gap-2">
          {[
            { value: 'all' as const, label: 'Tous', icon: null },
            { value: 'essence' as const, label: 'Essence', icon: Fuel },
            { value: 'electrique' as const, label: 'Électrique', icon: Zap },
          ].map((item) => (
            <button
              key={item.value}
              onClick={() => onFuelTypeChange(item.value)}
              className={cn(
                "flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs font-medium transition-all border",
                fuelType === item.value
                  ? "bg-primary text-white border-primary"
                  : "bg-[#0a0a0c] border-white/5 text-muted-foreground hover:border-white/20 hover:text-white"
              )}
            >
              {item.icon && <item.icon className="w-3.5 h-3.5" />}
              {item.label}
            </button>
          ))}
        </div>
      </div>

      {/* Grid View - Texte : Affichage, Liste, Grille */}
      <div className="space-y-3 pt-6 border-t border-white/5">
        <label className={labelStyle}>Affichage</label>
        <div className="flex p-1 bg-[#0a0a0c] rounded-xl border border-white/5">
          <button
            onClick={() => onGridViewChange(1)}
            className={cn(
              "flex-1 flex items-center justify-center gap-2 py-2.5 rounded-lg transition-all",
              gridView === 1 
                ? "bg-[#d1d1c7] text-[#1a1a1e] shadow-sm" 
                : "text-muted-foreground hover:text-white"
            )}
          >
            <List className="w-4 h-4" />
            <span className="text-xs font-medium">Liste</span>
          </button>
          <button
            onClick={() => onGridViewChange(3)}
            className={cn(
              "flex-1 flex items-center justify-center gap-2 py-2.5 rounded-lg transition-all",
              gridView === 3 
                ? "bg-[#d1d1c7] text-[#1a1a1e] shadow-sm" 
                : "text-muted-foreground hover:text-white"
            )}
          >
            <LayoutGrid className="w-4 h-4" />
            <span className="text-xs font-medium">Grille</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default VehicleFilters;