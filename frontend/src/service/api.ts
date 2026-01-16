import axios from 'axios';

// Configuration de base d'Axios
const api = axios.create({ 
  baseURL: 'http://10.49.20.91:8000/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

/**
 * AUTHENTIFICATION
 */
export const authApi = {
  // Inscrire un client : { nom, email, telephone, password, confirmPassword, parentId, societe }
  register: (data: any) => api.post('/auth/register', data),
  
 // Connexion avec stockage local
  login: async (data: any) => {
    console.log('=== LOGIN DEBUT ===');
    const response = await api.post('/auth/login', data);
    console.log('Réponse complète du backend:', response.data);
    console.log('Structure de response.data:', JSON.stringify(response.data, null, 2));
    
    if (response.data) {
      // Le backend retourne probablement { authenticated: true, clientId: X, ... } ou { authenticated: true, id: X, ... }
      // On s'assure que l'ID est présent sous une forme ou une autre
      const userData = {
        ...response.data,
        // Normaliser l'ID client
        clientId: response.data.clientId || response.data.id || response.data.userId,
        id: response.data.id || response.data.clientId || response.data.userId,
      };
      
      console.log('Données utilisateur à stocker:', userData);
      localStorage.setItem('user', JSON.stringify(userData));
      console.log('=== LOGIN FIN - DONNÉES STOCKÉES ===');
    }
    return response;
  },
  
  // Déconnexion avec nettoyage local
  logout: async (clientId: number | string) => {
    try {
      await api.delete(`/auth/logout/${clientId}`);
    } finally {
      // On retire les données même si l'appel API échoue (ex: session déjà expirée)
      localStorage.removeItem('user');
    }
  },

  // Helper pour récupérer l'utilisateur stocké
  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    const parsedUser = user ? JSON.parse(user) : null;
    console.log('Utilisateur récupéré du localStorage:', parsedUser);
    return parsedUser;
  },

  // Helper pour récupérer l'ID client de manière sûre
  getClientId: () => {
    const user = authApi.getCurrentUser();
    return user?.id || user?.clientId || null;
  },
};

/** 
*VÉHICULES & CATALOGUE
*/
export const vehiculeApi = {
  // GET /api/vehicules/catalogue
  getCatalogue: () => api.get('/vehicules/catalogue'),
  
  // GET /api/vehicules/soldes
  getVehiculesEnSoldes: () => api.get('/vehicules/soldes'),

  // GET /api/vehicules/soldables (Nouveau: pour l'admin)
  getVehiculesSoldables: () => api.get('/vehicules/soldables'),

  // POST /api/vehicules
  ajouter: (data: any) => api.post('/vehicules', data),
  
  // PUT /api/vehicules/{id}/solder?pourcentage=X
  solderVehicule: (id: number, pourcentage: number) => 
    api.put(`/vehicules/${id}/solder`, null, { params: { pourcentage } }),

  // GET /api/vehicules/{id}/calculer-prix
  calculerPrix: (id: number, optionsIds: number[]) => 
    api.get(`/vehicules/${id}/calculer-prix`, { params: { optionsIds } }),
};

/**
 * COMMANDES
 */
export const commandeApi = {
  // Passer une commande initiale (EN_COURS) : { clientId, vehiculesIds, typePaiement, paysLivraison }
  creer: (data: any) => {
    console.log('Données envoyées à /commande:', data);
    console.log('ClientId type:', typeof data.clientId, 'Value:', data.clientId);
    console.log('VehiculesIds:', data.vehiculesIds);
    return api.post('/commande', data);
  },
  
  // Valider une commande (Passe à VALIDE et génère la liasse)
  valider: (id: number | string) => api.put(`/commande/${id}/valider`),
  
  // Calculer le prix final avec options additionnelles (Body: { additionalProp1: [], ... })
  calculerPrixFinal: (id: number | string, optionsData: any) => 
    api.post(`/commande/${id}/prix-final`, optionsData),
};

/**
 * OPTIONS
 */
export const optionApi = {
  // Lister toutes les options disponibles
  lister: () => api.get('/options'),
  
  // Créer une nouvelle option : { id, nom, prix }
  creer: (data: any) => api.post('/options', data),
};

/**
 * STOCK (Gestion globale des soldes)
 */
export const stockApi = {
  // Solder massivement des véhicules : { pourcentage, marque }
  solder: (data: { pourcentage: number, marque: string }) => api.post('/stock/solder', data),
  
  // Annuler la dernière opération de solde massive
  annulerDernierSolde: () => api.post('/stock/annuler'),
};

/**
 * DOCUMENTS & LIASSE
 */
export const documentApi = {
  // Créer un document via l'Adapter
  genererPDF: (data: { titre: string, contenu: string }) => api.post('/documents/pdf', data),
  genererHTML: (data: { titre: string, contenu: string }) => api.post('/documents/html', data),
  
  // Récupérer l'instance unique (Singleton) de la liasse vierge contenant les titres/contenus
  getLiasseVierge: () => api.get('/liasse/vierge'),
};

/**
 * CLIENTS
 */
export const clientApi = {
  // Afficher les clients de la base de données
  lister: () => api.get('/client'),
  
  // Création d'un client : { nom, email, telephone, parentId, societe }
  creer: (data: any) => api.post('/client', data),
};

/**
 * FORMULAIRES
 */
export const formulaireApi = {
  // Générer un formulaire (Immatriculation) : { typeFormulaire, format }
  generer: (data: { typeFormulaire: string, format: string }) => 
    api.post('/formulaires/generer', data),
};

/**
 * DOCUMENTS
 */
export const documentsApi = {
  // Créer un document PDF
  creerPDF: (data: { titre: string, contenu: string }) =>
    api.post('/documents/pdf', data),
  
  // Créer un document HTML
  creerHTML: (data: { titre: string, contenu: string }) =>
    api.post('/documents/html', data),
};

/**
 * LIASSE
 */
export const liasseApi = {
  // Récupérer la liasse vierge
  getLiasseVierge: () =>
    api.get('/liasse/vierge'),
  
  // Générer une liasse pour une commande
  genererLiasse: (commandeId: number | string) =>
    api.get(`/liasse/${commandeId}`),
};

export default api;