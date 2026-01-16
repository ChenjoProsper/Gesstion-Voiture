import React from 'react';
import { Link } from 'react-router-dom';
import { Car, Mail, Phone, MapPin } from 'lucide-react';


const Footer: React.FC = () => {
  return (
    <footer className="bg-[#080808] border-t border-white/5 pt-24 pb-12">
      <div className="container mx-auto px-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-16 mb-20">
          {/* Brand */}
          <div className="space-y-6">
            <Link to="/" className="flex items-center gap-3">
              <div className="p-2.5 rounded-xl bg-primary shadow-lg shadow-primary/20">
                <Car className="w-6 h-6 text-white" />
              </div>
              <span className="text-xl font-display font-bold text-white tracking-tighter italic">
                Auto-Pattern
              </span>
            </Link>
            <p className="text-white/40 text-sm leading-relaxed font-light max-w-xs">
              L'excellence à l'état pur. Une sélection méticuleuse pour les passionnés de mobilité premium.
            </p>
          </div>

          {/* Navigation */}
          <div>
            <h4 className="font-display font-bold text-white mb-8 text-[10px] uppercase tracking-[0.3em] opacity-30">Exploration</h4>
            <ul className="space-y-4">
              {['Accueil', 'Catalogue', 'Connexion'].map((item) => (
                <li key={item}>
                  <Link to="/" className="text-white/50 hover:text-primary transition-all duration-300 text-sm font-light">
                    {item}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h4 className="font-display font-bold text-white mb-8 text-[10px] uppercase tracking-[0.3em] opacity-30">Contact</h4>
            <ul className="space-y-5">
              <li className="flex items-center gap-4 text-white/50 text-sm group cursor-pointer">
                <div className="p-2 rounded-lg bg-white/5 group-hover:bg-primary/20 transition-colors">
                  <Mail className="w-4 h-4" />
                </div>
                contact@auto-pattern.fr
              </li>
              <li className="flex items-center gap-4 text-white/50 text-sm group cursor-pointer">
                <div className="p-2 rounded-lg bg-white/5 group-hover:bg-primary/20 transition-colors">
                  <Phone className="w-4 h-4" />
                </div>
                +33 1 23 45 67 89
              </li>
            </ul>
          </div>

          {/* Newsletter / Legal Subtle */}
          <div>
            <h4 className="font-display font-bold text-white mb-8 text-[10px] uppercase tracking-[0.3em] opacity-30">Légal</h4>
            <div className="flex flex-col gap-4">
              <a href="#" className="text-white/30 hover:text-white text-xs transition-colors">Politique de Confidentialité</a>
              <a href="#" className="text-white/30 hover:text-white text-xs transition-colors">Mentions Légales</a>
              <div className="pt-4 flex gap-4">
                 {/* Social placeholders */}
                 <div className="w-8 h-8 rounded-full border border-white/10 flex items-center justify-center text-white/40 hover:border-primary hover:text-primary transition-all cursor-pointer">f</div>
                 <div className="w-8 h-8 rounded-full border border-white/10 flex items-center justify-center text-white/40 hover:border-primary hover:text-primary transition-all cursor-pointer">in</div>
              </div>
            </div>
          </div>
        </div>

        <div className="pt-12 border-t border-white/5 flex flex-col md:flex-row justify-between items-center gap-6">
          <p className="text-white/20 text-[10px] uppercase tracking-widest font-bold">
            © 2024 Auto-Pattern. Conçu pour l'excellence.
          </p>
          <div className="flex gap-8">
            <span className="text-white/10 text-[10px] uppercase tracking-[0.2em]">Paris</span>
            <span className="text-white/10 text-[10px] uppercase tracking-[0.2em]">Monaco</span>
            <span className="text-white/10 text-[10px] uppercase tracking-[0.2em]">Genève</span>
          </div>
        </div>
      </div>
    </footer>
  );
};
export default Footer;
