import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '@/components/layout/Navbar';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group';
import { useAuth } from '@/context/AuthContext';
import { UserType } from '@/types/vehicle';
import { Car, User, Building2, Mail, Lock, ArrowRight, Phone } from 'lucide-react';
import { cn } from '@/lib/utils';
import { authApi } from '@/service/api'; 

const Auth: React.FC = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [telephone, setTelephone] = useState('');
  const [userType, setUserType] = useState<UserType>('particulier');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { setUser } = useAuth(); 
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
     if (isLogin) {
        // --- LOGIQUE DE CONNEXION ---
        const response = await authApi.login({
          email,
          password,
        });

        // On vérifie si la réponse contient des données utilisateur
        if (response.data) {
          setUser(response.data);
          localStorage.setItem('user', JSON.stringify(response.data));
          
          // --- AJOUT DE LA REDIRECTION CONDITIONNELLE ---
          if (email.toLowerCase().includes('admin')) {
            navigate('/admin');
          } else {
            navigate('/');
          }
        }
      } else {
        // --- LOGIQUE D'INSCRIPTION ---
        // On construit l'objet EXACT attendu par le backend
        const registerData = {
          nom: name,
          email: email,
          telephone: telephone,
          password: password,
          confirmPassword: password, // Doit correspondre au password
          societe: userType === 'societe', // Transforme le choix en booléen
        };

        const response = await authApi.register(registerData);

        if (response.data) {
          // Après inscription, on bascule vers la connexion
          setIsLogin(true);
          setError('Inscription réussie ! Connectez-vous maintenant.');
        }
      }
    } catch (err: any) {
      console.error("Détails de l'erreur API:", err);
      // Récupère le message d'erreur du backend s'il existe
      const errorMessage = err.response?.data?.message || 
                          err.response?.data || 
                          'Une erreur est survenue lors de la communication avec le serveur.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen"style={{ backgroundColor: 'hsl(var(--bg))' }}>
      <Navbar />
      <main className="pt-32 pb-24">
        <div className="container px-4">
          <div className="max-w-md mx-auto">
            {/* Logo & Header */}
            <div className="text-center mb-8">
              <div className="w-16 h-16 rounded-2xl bg-[#1E293B]/5 flex items-center justify-center mx-auto mb-4 border border-[#1E293B]/10">
                <Car className="w-8 h-8 text-[#1E293B]" />
              </div>
              <h1 className="text-3xl font-bold text-[#1E293B] mb-2 tracking-tight" style={{ fontFamily: 'Space Grotesk, sans-serif' }}>
                {isLogin ? 'Bienvenue' : 'Rejoignez-nous'}
              </h1>
              <p className="text-slate-500" style={{ fontFamily: 'Satoshi, sans-serif' }}>
                {isLogin 
                  ? 'Accédez à votre espace Auto-Pattern Luxury'
                  : 'Créez votre profil pour gérer vos acquisitions'}
              </p>
            </div>

            <div className="bg-white rounded-3xl shadow-sm border border-slate-100 p-8">
              {/* Switch Login/Register */}
              <div className="flex rounded-2xl bg-slate-50 p-1.5 mb-8">
                <button
                  type="button"
                  onClick={() => { setIsLogin(true); setError(''); }}
                  className={cn(
                    "flex-1 py-3 text-sm font-semibold rounded-xl transition-all duration-300",
                    isLogin 
                      ? "bg-white text-[#1E293B] shadow-sm" 
                      : "text-slate-400 hover:text-slate-600"
                  )}
                >
                  Connexion
                </button>
                <button
                  type="button"
                  onClick={() => { setIsLogin(false); setError(''); }}
                  className={cn(
                    "flex-1 py-3 text-sm font-semibold rounded-xl transition-all duration-300",
                    !isLogin 
                      ? "bg-white text-[#1E293B] shadow-sm" 
                      : "text-slate-400 hover:text-slate-600"
                  )}
                >
                  Inscription
                </button>
              </div>

              <form onSubmit={handleSubmit} className="space-y-5">
                {!isLogin && (
                  <>
                    <div className="space-y-2">
                      <Label htmlFor="name">Nom complet ou Raison Sociale</Label>
                      <div className="relative">
                        <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                        <Input
                          id="name"
                          value={name}
                          onChange={(e) => setName(e.target.value)}
                          className="pl-10 h-12 rounded-xl bg-slate-50/50 border-slate-100"
                          required
                        />
                      </div>
                    </div>

                    <div className="space-y-2">
                      <Label htmlFor="telephone">Téléphone</Label>
                      <div className="relative">
                        <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                        <Input
                          id="telephone"
                          value={telephone}
                          onChange={(e) => setTelephone(e.target.value)}
                          className="pl-10 h-12 rounded-xl bg-slate-50/50 border-slate-100"
                          required
                        />
                      </div>
                    </div>

                    <div className="space-y-3">
                      <Label>Type de profil</Label>
                      <RadioGroup
                        value={userType}
                        onValueChange={(value) => setUserType(value as UserType)}
                        className="grid grid-cols-2 gap-4"
                      >
                        <Label
                          htmlFor="particulier"
                          className={cn(
                            "flex flex-col items-center p-4 rounded-2xl border-2 cursor-pointer transition-all",
                            userType === 'particulier'
                              ? "border-[#1E293B] bg-[#1E293B]/5"
                              : "border-slate-50 hover:border-slate-200"
                          )}
                        >
                          <RadioGroupItem value="particulier" id="particulier" className="sr-only" />
                          <User className="w-6 h-6 mb-2" />
                          <span className="text-xs font-bold uppercase tracking-wider">Particulier</span>
                        </Label>
                        <Label
                          htmlFor="societe"
                          className={cn(
                            "flex flex-col items-center p-4 rounded-2xl border-2 cursor-pointer transition-all",
                            userType === 'societe'
                              ? "border-[#1E293B] bg-[#1E293B]/5"
                              : "border-slate-50 hover:border-slate-200"
                          )}
                        >
                          <RadioGroupItem value="societe" id="societe" className="sr-only" />
                          <Building2 className="w-6 h-6 mb-2" />
                          <span className="text-xs font-bold uppercase tracking-wider">Société</span>
                        </Label>
                      </RadioGroup>
                    </div>
                  </>
                )}

                <div className="space-y-2">
                  <Label htmlFor="email">Adresse Email</Label>
                  <div className="relative">
                    <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <Input
                      id="email"
                      type="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      className="pl-10 h-12 rounded-xl bg-slate-50/50 border-slate-100"
                      required
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="password">Mot de passe</Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <Input
                      id="password"
                      type="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      className="pl-10 h-12 rounded-xl bg-slate-50/50 border-slate-100"
                      required
                    />
                  </div>
                </div>

                {error && (
                  <div className={cn(
                    "p-3 rounded-lg text-sm text-center font-medium",
                    error.includes("réussie") 
                      ? "bg-green-50 border border-green-100 text-green-600" 
                      : "bg-red-50 border border-red-100 text-red-600"
                  )}>
                    {error}
                  </div>
                )}

                <Button
                  type="submit"
                  disabled={loading}
                  className="w-full h-14 rounded-2xl bg-[#1E293B] hover:bg-[#0F172A] text-white font-bold transition-all shadow-lg shadow-slate-200"
                >
                  {loading ? (
                    <span className="flex items-center gap-2">Traitement en cours...</span>
                  ) : (
                    <span className="flex items-center gap-2">
                      {isLogin ? 'Accéder à mon espace' : 'Créer mon compte'}
                      <ArrowRight className="w-4 h-4" />
                    </span>
                  )}
                </Button>
              </form>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Auth;