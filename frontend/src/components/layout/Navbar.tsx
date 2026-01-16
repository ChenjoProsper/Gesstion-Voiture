import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Car, ShoppingCart, User, Menu, X, Settings, Building2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useCart } from '@/context/CartContext';
import { useAuth } from '@/context/AuthContext';
import { cn } from '@/lib/utils';
import { motion, AnimatePresence } from 'framer-motion';

const Navbar: React.FC = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const { itemCount } = useCart();
  const { isAuthenticated, user, logout, isAdmin, isSociete } = useAuth();
  const location = useLocation();

  const navLinks = [
    { to: '/', label: 'Accueil' },
    { to: '/catalogue', label: 'Catalogue' },
    { to: '/documents', label: 'Documents', auth: true },
  ];

  const isActive = (path: string) => location.pathname === path;

  return (
   <nav className="fixed top-0 left-0 right-0 z-50 transition-all duration-300 border-b border-white/5 bg-[#050505]/40 backdrop-blur-md">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-20">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-3 group">
            <div className="p-2 rounded-lg bg-white/10 group-hover:bg-primary transition-all duration-300">
              <Car className="w-5 h-5 text-white" />
            </div>
            <span className="text-lg font-display font-semibold text-white tracking-tight uppercase border-l border-white/20 pl-3">
              Auto-Pattern
            </span>
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center gap-10">
            {navLinks.map(link => (
              (!link.auth || isAuthenticated) && (
                <Link
                  key={link.to}
                  to={link.to}
                  className={cn(
                    "text-[11px] font-bold tracking-[0.2em] uppercase transition-all duration-300 relative py-2",
                    isActive(link.to) ? "text-white" : "text-white/40 hover:text-white"
                  )}
                >
                  {link.label}
                  {isActive(link.to) && (
                    <motion.div 
                      className="absolute -bottom-1 left-0 right-0 h-[1px] bg-primary rounded-full"
                      layoutId="navbar-indicator"
                    />
                  )}
                </Link>
              )
            ))}
          </div>

          {/* Right Actions */}
          <div className="hidden md:flex items-center gap-4">
            <Link to="/panier" className="relative group">
              <Button variant="ghost" size="icon" className="rounded-full text-white/60 hover:text-white hover:bg-white/10">
                <ShoppingCart className="w-5 h-5" />
                {itemCount > 0 && (
                  <span className="absolute top-0 right-0 w-4 h-4 bg-primary text-[10px] text-white rounded-full flex items-center justify-center font-bold">
                    {itemCount}
                  </span>
                )}
              </Button>
            </Link>

            {isAuthenticated ? (
               <div className="flex items-center gap-3 pl-4 border-l border-white/10">
                  <span className="text-xs font-medium text-white/50 lowercase italic">
                    {user?.name}
                  </span>
                  <Button variant="outline" size="sm" onClick={logout} className="rounded-full bg-transparent border-white/20 text-white hover:bg-white hover:text-black text-[10px] uppercase font-bold tracking-widest px-4">
                    Logout
                  </Button>
               </div>
            ) : (
              <Link to="/connexion">
                <Button variant="hero" size="sm" className="rounded-full px-6 text-[10px] uppercase tracking-tighter">
                  Connexion
                </Button>
              </Link>
            )}
          </div>
          {/* ... bouton mobile ... */}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
