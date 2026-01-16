import React from 'react';
import { Car, Users, Award, Clock } from 'lucide-react';
import { motion } from 'framer-motion';

const stats = [
  { icon: Car, value: '500+', label: 'Véhicules vendus' },
  { icon: Users, value: '2000+', label: 'Clients satisfaits' },
  { icon: Award, value: '15', label: 'Années d\'expertise' },
  { icon: Clock, value: '24/7', label: 'Support disponible' },
];

const StatsSection: React.FC = () => {
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: { duration: 0.5 },
    },
  };

  return (
    <section className="py-24 bg-card relative overflow-hidden">
      {/* Subtle background */}
      <div className="absolute inset-0">
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[400px] bg-violet-pastel/20 blur-[150px]" />
      </div>

      <div className="container px-4 relative z-10">
        <motion.div 
          className="grid grid-cols-2 lg:grid-cols-4 gap-8"
          variants={containerVariants}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
        >
          {stats.map((stat, index) => (
            <motion.div
              key={index}
              className="text-center group"
              variants={itemVariants}
            >
              <motion.div 
                className="w-16 h-16 rounded-2xl bg-accent flex items-center justify-center mx-auto mb-5 group-hover:bg-primary/10 transition-colors"
                whileHover={{ scale: 1.05 }}
                transition={{ duration: 0.2 }}
              >
                <stat.icon className="w-8 h-8 text-primary" />
              </motion.div>
              <p className="text-4xl md:text-5xl font-display font-bold text-gradient mb-2">
                {stat.value}
              </p>
              <p className="text-muted-foreground">{stat.label}</p>
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  );
};

export default StatsSection;
