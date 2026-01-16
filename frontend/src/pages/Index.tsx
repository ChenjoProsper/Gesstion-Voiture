import React from 'react';
import Navbar from '@/components/layout/Navbar';
import Footer from '@/components/layout/Footer';
import HeroSection from '@/components/home/HeroSection';
import FeaturedVehicles from '@/components/home/FeaturedVehicles';
import StatsSection from '@/components/home/StatsSection';

const Index: React.FC = () => {
  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main>
        <HeroSection />
        <FeaturedVehicles />
        <StatsSection />
      </main>
      <Footer />
    </div>
  );
};

export default Index;
