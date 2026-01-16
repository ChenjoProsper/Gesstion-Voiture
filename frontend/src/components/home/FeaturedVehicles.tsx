import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import VehicleCard from '@/components/vehicles/VehicleCard';
import { vehicles } from '@/data/vehicles';
import { motion } from 'framer-motion';

const FeaturedVehicles: React.FC = () => {
  const featuredVehicles = vehicles.slice(0, 3);

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.15,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 30 },
    visible: {
      opacity: 1,
      y: 0,
      transition: { duration: 0.6 },
    },
  };

  return (
    <section className="py-28 bg-secondary/50 relative" style={{ backgroundColor: 'hsl(var(--bg))' }} >
      {/* Background accent */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute top-0 left-1/4 w-[600px] h-[400px] bg-violet-pastel/30 blur-[150px]" />
        <div className="absolute bottom-0 right-1/4 w-[500px] h-[300px] bg-primary/5 blur-[120px]" />
      </div>

      <div className="container relative z-10 px-4">
        {/* Header */}
        <motion.div 
          className="flex flex-col md:flex-row items-start md:items-end justify-between gap-6 mb-16"
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.6 }}
        >
          <div>
            <p className="text-primary font-medium mb-3">Nos Véhicules</p>
            <h2 className="text-4xl md:text-5xl font-display font-bold text-foreground">
              Sélection <span className="text-gradient">Premium</span>
            </h2>
          </div>
          <Link to="/catalogue">
            <Button variant="outline" size="lg" className="rounded-xl">
              Voir tout le catalogue
              <ArrowRight className="w-4 h-4" />
            </Button>
          </Link>
        </motion.div>

        {/* Vehicles Grid */}
        <motion.div 
          className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8"
          variants={containerVariants}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
        >
          {featuredVehicles.map((vehicle) => (
            <motion.div key={vehicle.id} variants={itemVariants}>
              <VehicleCard vehicle={vehicle} />
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  );
};

export default FeaturedVehicles;
