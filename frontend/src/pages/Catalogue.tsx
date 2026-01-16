 /*import React, { useState, useMemo } from 'react';

import Navbar from '@/components/layout/Navbar';

import Footer from '@/components/layout/Footer';

import VehicleCard from '@/components/vehicles/VehicleCard';

import VehicleFilters from '@/components/vehicles/VehicleFilters';

import { vehicles } from '@/data/vehicles';

import { cn } from '@/lib/utils';

import { motion } from 'framer-motion';


const Catalogue: React.FC = () => {

const [search, setSearch] = useState('');

const [vehicleType, setVehicleType] = useState<'all' | 'automobile' | 'scooter'>('all');

const [fuelType, setFuelType] = useState<'all' | 'essence' | 'electrique'>('all');

const [gridView, setGridView] = useState<1 | 3>(3);

const [searchOperator, setSearchOperator] = useState<'ET' | 'OU'>('ET');


const filteredVehicles = useMemo(() => {

return vehicles.filter(vehicle => {

if (vehicleType !== 'all' && vehicle.type !== vehicleType) return false;

if (fuelType !== 'all' && vehicle.fuelType !== fuelType) return false;

if (search) {

const searchTerms = search.toLowerCase().split(' ').filter(Boolean);

const vehicleText = `${vehicle.brand} ${vehicle.model} ${vehicle.color}`.toLowerCase();

return searchOperator === 'ET'

? searchTerms.every(term => vehicleText.includes(term))

: searchTerms.some(term => vehicleText.includes(term));

}

return true;

});

}, [search, vehicleType, fuelType, searchOperator]);


return (

<div className="min-h-screen bg-[#050505] text-white">

<Navbar />



<div className="fixed inset-0 pointer-events-none">

<div className="absolute top-0 right-0 w-[500px] h-[500px] bg-primary/10 rounded-full blur-[120px] opacity-50" />

<div className="absolute bottom-0 left-0 w-[500px] h-[500px] bg-white/[0.02] rounded-full blur-[120px]" />

</div>


<main className="relative pt-40 pb-24 z-10">

<div className="container px-6">



<header className="mb-16 border-l border-white/10 pl-8">

<motion.p

initial={{ opacity: 0, x: -20 }}

animate={{ opacity: 1, x: 0 }}

className="text-primary font-bold tracking-[0.3em] uppercase text-[10px] mb-4"

>

Collection Privée

</motion.p>

<motion.h1

initial={{ opacity: 0, y: 20 }}

animate={{ opacity: 1, y: 0 }}

className="text-5xl md:text-6xl font-display font-semibold mb-6 tracking-tighter"

>

Le <span className="text-white/40 italic font-serif">Catalogue</span>

</motion.h1>

<motion.p

initial={{ opacity: 0 }}

animate={{ opacity: 1 }}

transition={{ delay: 0.2 }}

className="text-white/40 max-w-2xl font-light leading-relaxed"

>

Une immersion dans l'ingénierie d'exception. Sélectionnez votre futur standard de mobilité parmi nos modèles essence et électriques.

</motion.p>

</header>



<div className="grid grid-cols-1 lg:grid-cols-4 gap-12">



<aside className="lg:col-span-1">

<div className="sticky top-28 p-6 rounded-2xl bg-white/[0.02] border border-white/[0.05] backdrop-blur-md">

<h3 className="text-[10px] font-bold uppercase tracking-[0.2em] mb-6 text-white/60">Filtres de recherche</h3>

<VehicleFilters

search={search}

onSearchChange={setSearch}

vehicleType={vehicleType}

onVehicleTypeChange={setVehicleType}

fuelType={fuelType}

onFuelTypeChange={setFuelType}

gridView={gridView}

onGridViewChange={setGridView}

searchOperator={searchOperator}

onSearchOperatorChange={setSearchOperator}

/>

</div>

</aside>



<div className="lg:col-span-3">

<div className="flex items-center justify-between mb-8 pb-4 border-b border-white/5">

<p className="text-[10px] font-bold uppercase tracking-widest text-white/30">

Total: <span className="text-white">{filteredVehicles.length}</span> Modèles

</p>



<div className="text-[10px] uppercase text-white/20 tracking-widest font-bold">

Affichage {gridView === 1 ? 'Liste' : 'Grille'}

</div>

</div>


{filteredVehicles.length > 0 ? (

<div

className={cn(

"grid gap-8 transition-all duration-500",

gridView === 1 ? "grid-cols-1" : "grid-cols-1 md:grid-cols-2 xl:grid-cols-3"

)}

>

{filteredVehicles.map((vehicle, index) => (

<motion.div

key={vehicle.id}

initial={{ opacity: 0, y: 20 }}

animate={{ opacity: 1, y: 0 }}

transition={{ delay: index * 0.05 }}

>

<VehicleCard vehicle={vehicle} compact={gridView === 1} />

</motion.div>

))}

</div>

) : (

<motion.div

initial={{ opacity: 0 }}

animate={{ opacity: 1 }}

className="text-center py-32 bg-white/[0.01] rounded-3xl border border-dashed border-white/10"

>

<p className="text-lg text-white/60 font-light mb-2">

Aucun résultat trouvé.

</p>

<p className="text-xs text-white/20 uppercase tracking-widest">

Ajustez vos critères pour une nouvelle recherche.

</p>

</motion.div>

)}

</div>

</div>

</div>

</main>


<Footer />

</div>

);

};


export default Catalogue
*/
import React, { useState, useMemo, useEffect } from 'react';
import Navbar from '@/components/layout/Navbar';
import Footer from '@/components/layout/Footer';
import VehicleCard from '@/components/vehicles/VehicleCard';
import VehicleFilters from '@/components/vehicles/VehicleFilters';
import { cn } from '@/lib/utils';
import { motion } from 'framer-motion';
import { vehiculeApi } from '@/service/api';
import { Vehicle } from '@/types/vehicle';

const Catalogue: React.FC = () => {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [vehicleType, setVehicleType] = useState<'all' | 'automobile' | 'scooter'>('all');
  const [fuelType, setFuelType] = useState<'all' | 'essence' | 'electrique'>('all');
  const [gridView, setGridView] = useState<1 | 3>(3);
  const [searchOperator, setSearchOperator] = useState<'ET' | 'OU'>('ET');

  // Récupération des données via l'API
  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        setLoading(true);
        const response = await vehiculeApi.getCatalogue();
        
        // Sécurité: on s'assure que c'est un tableau
        setVehicles(Array.isArray(response.data) ? response.data : []);
      } catch (error) {
        console.error("Erreur lors du chargement du catalogue:", error);
        setVehicles([]);
      } finally {
        setLoading(false);
      }
    };
    fetchVehicles();
  }, []);

  const filteredVehicles = useMemo(() => {
    return vehicles.filter(vehicle => {
    
      const type = vehicle.typeVehicule || ""; 
      
      const isAuto = type.includes('AUTO');
      const isScooter = type.includes('SCOOTER');
      const isElectric = type.includes('ELECTRIQUE');

      // Application des filtres de type
      if (vehicleType === 'automobile' && !isAuto) return false;
      if (vehicleType === 'scooter' && !isScooter) return false;
      
      // Application des filtres de carburant
      if (fuelType === 'electrique' && !isElectric) return false;
      if (fuelType === 'essence' && isElectric) return false;

      // Recherche textuelle sécurisée
      if (search) {
        const searchTerms = search.toLowerCase().split(' ').filter(Boolean);
        const marque = vehicle.marque || "";
        const modele = vehicle.modele || "";
        const desc = vehicle.description || "";
        
        const vehicleText = `${marque} ${modele} ${desc}`.toLowerCase();
        
        return searchOperator === 'ET' 
          ? searchTerms.every(term => vehicleText.includes(term))
          : searchTerms.some(term => vehicleText.includes(term));
      }
      return true;
    });
  }, [vehicles, search, vehicleType, fuelType, searchOperator]);

  return (
    <div className="min-h-screen bg-[#050505] text-white">
      <Navbar />
      
      <div className="fixed inset-0 pointer-events-none">
        <div className="absolute top-0 right-0 w-[500px] h-[500px] bg-primary/10 rounded-full blur-[120px] opacity-50" />
        <div className="absolute bottom-0 left-0 w-[500px] h-[500px] bg-white/[0.02] rounded-full blur-[120px]" />
      </div>

      <main className="relative pt-40 pb-24 z-10">
        <div className="container px-6">
          <header className="mb-16 border-l border-white/10 pl-8">
            <motion.p initial={{ opacity: 0, x: -20 }} animate={{ opacity: 1, x: 0 }} className="text-primary font-bold tracking-[0.3em] uppercase text-[10px] mb-4">
              Collection Privée
            </motion.p>
            <motion.h1 initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="text-5xl md:text-6xl font-display font-semibold mb-6 tracking-tighter">
              Le <span className="text-white/40 italic font-serif">Catalogue</span>
            </motion.h1>
            <motion.p initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ delay: 0.2 }} className="text-white/40 max-w-2xl font-light leading-relaxed">
              Une immersion dans l'ingénierie d'exception. Sélectionnez votre futur standard de mobilité parmi nos modèles essence et électriques.
            </motion.p>
          </header>

          <div className="grid grid-cols-1 lg:grid-cols-4 gap-12">
            <aside className="lg:col-span-1">
              <div className="sticky top-28 p-6 rounded-2xl bg-white/[0.02] border border-white/[0.05] backdrop-blur-md">
                <h3 className="text-[10px] font-bold uppercase tracking-[0.2em] mb-6 text-white/60">Filtres de recherche</h3>
                <VehicleFilters
                  search={search}
                  onSearchChange={setSearch}
                  vehicleType={vehicleType}
                  onVehicleTypeChange={setVehicleType}
                  fuelType={fuelType}
                  onFuelTypeChange={setFuelType}
                  gridView={gridView}
                  onGridViewChange={setGridView}
                  searchOperator={searchOperator}
                  onSearchOperatorChange={setSearchOperator}
                />
              </div>
            </aside>

            <div className="lg:col-span-3">
              <div className="flex items-center justify-between mb-8 pb-4 border-b border-white/5">
                <p className="text-[10px] font-bold uppercase tracking-widest text-white/30">
                  Total: <span className="text-white">{loading ? '...' : filteredVehicles.length}</span> Modèles
                </p>
                <div className="text-[10px] uppercase text-white/20 tracking-widest font-bold">
                  Affichage {gridView === 1 ? 'Liste' : 'Grille'}
                </div>
              </div>

              {loading ? (
                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-8">
                   {[1,2,3,4,5,6].map(i => (
                     <div key={i} className="h-80 bg-white/5 animate-pulse rounded-2xl" />
                   ))}
                </div>
              ) : filteredVehicles.length > 0 ? (
                <div className={cn("grid gap-8 transition-all duration-500", gridView === 1 ? "grid-cols-1" : "grid-cols-1 md:grid-cols-2 xl:grid-cols-3")}>
                  {filteredVehicles.map((vehicle, index) => (
                    <motion.div key={vehicle.id} initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: index * 0.05 }}>
                      <VehicleCard vehicle={vehicle} compact={gridView === 1} />
                    </motion.div>
                  ))}
                </div>
              ) : (
                <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="text-center py-32 bg-white/[0.01] rounded-3xl border border-dashed border-white/10">
                  <p className="text-lg text-white/60 font-light mb-2">Aucun résultat trouvé.</p>
                </motion.div>
              )}
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default Catalogue;