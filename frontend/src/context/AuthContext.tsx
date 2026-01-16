import React, { createContext, useContext, useState, useEffect } from 'react';
import { User, UserType } from '@/types/vehicle';
import { authApi } from '@/service/api'; // Import de tes services axios

interface AuthContextType {
  user: any | null; // Changé en any pour correspondre au retour DTO de ton backend
  isAuthenticated: boolean;
  isAdmin: boolean;
  isSociete: boolean;
  setUser: (user: any) => void; // Ajout de setUser ici pour corriger l'erreur TS
  login: (email: string, password: string) => Promise<boolean>;
  register: (data: any) => Promise<boolean>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<any | null>(null);

  // Charger l'utilisateur au démarrage si présent dans le localStorage
  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      const response = await authApi.login({ email, password });
      if (response.data && response.data.authenticated) {
        console.log('Réponse login complète:', response.data);
        console.log('Keys dans response.data:', Object.keys(response.data));
        setUser(response.data);
        localStorage.setItem('user', JSON.stringify(response.data));
        return true;
      }
      return false;
    } catch (error) {
      console.error("Erreur Login:", error);
      return false;
    }
  };

  const register = async (data: any): Promise<boolean> => {
    try {
      const response = await authApi.register(data);
      if (response.data && response.data.clientId) {
        // Optionnel : on connecte l'utilisateur direct après inscription
        setUser(response.data);
        localStorage.setItem('user', JSON.stringify(response.data));
        return true;
      }
      return false;
    } catch (error) {
      console.error("Erreur Register:", error);
      return false;
    }
  };

  const logout = () => {
    const clientId = user?.clientId || user?.id || 0;
    console.log('Logout - ClientId utilisé:', clientId);
    authApi.logout(clientId);
    setUser(null);
    localStorage.removeItem('user');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isAdmin: user?.email?.includes('admin'), // Logique simple pour ton test
        isSociete: user?.societe === true, // Utilise le booléen de ton API
        setUser, // On l'expose enfin !
        login,
        register,
        logout
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};