import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { ArrowRight, Shield, Zap, Sparkles } from 'lucide-react';
import { motion, useScroll, useTransform, Variants } from 'framer-motion';

const HeroSection: React.FC = () => {
  const { scrollY } = useScroll();
  const yBg = useTransform(scrollY, [0, 500], [0, 100]);

  // Typage explicite des variants pour corriger l'erreur TS
  const containerVariants: Variants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { 
        staggerChildren: 0.1, 
        delayChildren: 0.2 
      },
    },
  };

  const itemVariants: Variants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: { 
        duration: 0.8, 
        ease: "easeOut" // Désormais accepté grâce au type Variants
      },
    },
  };

  return (
    <section className="relative h-screen min-h-[750px] flex flex-col items-center justify-center overflow-hidden bg-[#050505]">
      
      {/* Background Image avec traitement sombre profond */}
      <motion.div style={{ y: yBg }} className="absolute inset-0 z-0">
        <div className="absolute inset-0 bg-gradient-to-b from-black/40 via-[#050505]/80 to-[#050505] z-10" />
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,_transparent_0%,_#050505_90%)] z-10" />
        
        <img 
          src="https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=2070&auto=format&fit=crop" 
          alt="Luxury Automotive"
          className="w-full h-full object-cover opacity-30 scale-110"
        />
      </motion.div>

      {/* Content */}
      <motion.div 
        className="container relative z-20 px-4 w-full"
        variants={containerVariants}
        initial="hidden"
        animate="visible"
      >
        <div className="max-w-4xl mx-auto text-center">
          {/* Badge */}
          <motion.div variants={itemVariants} className="mb-4 mt-10">
            <span className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-white/5 border border-white/10 backdrop-blur-md">
              <span className="text-[10px] font-bold text-white/60 tracking-[0.2em] uppercase">Collection 2024</span>
            </span>
          </motion.div>

          {/* Title */}
          <motion.h1 
            variants={itemVariants}
            className="text-5xl md:text-7xl lg:text-8xl font-display font-semibold mb-6 tracking-tight leading-[1.1]"
          >
            <span className="text-white">L'Excellence</span>
            <br />
            <span className="text-white/30">Automobile</span>
          </motion.h1>

          {/* Subtitle */}
          <motion.p 
            variants={itemVariants}
            className="text-base md:text-lg text-white/50 max-w-xl mx-auto mb-10 leading-relaxed font-light"
          >
            Découvrez notre sélection de véhicules premium.
            Automobiles et scooters, essence ou électrique.
          </motion.p>

          {/* CTA Buttons */}
          <motion.div 
            variants={itemVariants}
            className="flex flex-col sm:flex-row items-center justify-center gap-4 mb-16"
          >
            <Link to="/catalogue">
              <Button variant="hero" size="xl" className="shadow-[0_0_30px_rgba(255,255,255,0.05)] h-14 px-10">
                Explorer le catalogue
                <ArrowRight className="w-5 h-5 ml-2" />
              </Button>
            </Link>
            <Link to="/connexion">
              <Button variant="glass" size="xl" className="h-14 px-10">
                Créer un compte
              </Button>
            </Link>
          </motion.div>

          {/* Features - Grid compacte */}
          <motion.div 
            variants={itemVariants}
            className="grid grid-cols-1 md:grid-cols-3 gap-4"
          >
            {[
              { icon: Shield, title: 'Garantie Premium', desc: 'Véhicules certifiés.' },
              { icon: Zap, title: 'Électrique', desc: 'Gamme 100% propre.' },
              { icon: Sparkles, title: 'Sur-mesure', desc: 'Service personnalisé.' },
            ].map((feature) => (
              <motion.div
                key={feature.title}
                className="p-5 rounded-xl bg-white/[0.03] border border-white/[0.08] backdrop-blur-sm hover:bg-white/[0.06] transition-all group text-center"
                whileHover={{ y: -5 }}
              >
                <div className="w-10 h-10 rounded-lg bg-white/5 flex items-center justify-center mb-3 mx-auto group-hover:scale-110 transition-transform">
                  <feature.icon className="w-5 h-5 text-white/80" />
                </div>
                <h3 className="font-display font-medium text-white mb-1 text-sm uppercase tracking-wide">{feature.title}</h3>
                <p className="text-xs text-white/40">{feature.desc}</p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </motion.div>

      {/* Scroll Indicator */}
      <motion.div 
        className="absolute bottom-8 left-1/2 -translate-x-1/2"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1.5 }}
      >
        <div className="w-6 h-10 rounded-full border border-white/10 flex justify-center pt-1.5 backdrop-blur-sm">
          <motion.div 
            className="w-1 h-1.5 bg-white/40 rounded-full"
            animate={{ y: [0, 12, 0] }}
            transition={{ duration: 2, repeat: Infinity }}
          />
        </div>
      </motion.div>
    </section>
  );
};

export default HeroSection;