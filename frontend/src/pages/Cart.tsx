import React, { useState, useEffect, useMemo } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Navbar from '@/components/layout/Navbar';
import Footer from '@/components/layout/Footer';
import { Button } from '@/components/ui/button';
import { useCart } from '@/context/CartContext';
import { useStock } from '@/context/StockContext';
import { taxRates } from '@/data/vehicles';
import OrderSuccessModal from '@/components/order/OrderSuccessModal';
import { Trash2, Plus, Minus, Undo2, CreditCard, Landmark, ShoppingBag, Loader2, Settings2 } from 'lucide-react';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Checkbox } from '@/components/ui/checkbox';
import { cn } from '@/lib/utils';
import { commandeApi, optionApi, liasseApi } from '@/service/api';
import { toast } from 'sonner';

const Cart: React.FC = () => {
  const { items, removeFromCart, updateQuantity, addOption, removeOption, clearCart, undo, canUndo } = useCart();
  const { isSoldered, getDiscount } = useStock();
  const navigate = useNavigate();

  // --- ÉTATS ---
  const [country, setCountry] = useState('France');
  const [paymentMethod, setPaymentMethod] = useState<'COMPTANT' | 'CREDIT'>('COMPTANT');
  const [showOrderSuccess, setShowOrderSuccess] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [availableOptions, setAvailableOptions] = useState<any[]>([]);
  const [createdOrder, setCreatedOrder] = useState<any>(null);
  const [orderTotal, setOrderTotal] = useState<number | null>(null);
  const [orderTaxAmount, setOrderTaxAmount] = useState<number | null>(null);
  const [liasseDocuments, setLiasseDocuments] = useState<any[]>([]);

  // --- CHARGEMENT DES OPTIONS DEPUIS LE BACKEND ---
  useEffect(() => {
    const fetchOptions = async () => {
      try {
        const response = await optionApi.lister(); // GET /api/options
        setAvailableOptions(response.data);
      } catch (error) {
        console.error("Erreur lors de la récupération des options:", error);
        toast.error("Impossible de charger les options de personnalisation");
      }
    };
    fetchOptions();
  }, []);

  const taxRate = taxRates[country] || 0.20;

  // --- CALCULS DES PRIX ---
  const calculateTotal = () => {
    return items.reduce((sum, item) => {
      const optionsTotal = item.options.reduce((optSum, opt) => optSum + opt.prix, 0); // Utilise 'prix' du backend
      const vehiclePrice = isSoldered(String(item.vehicle.id))
        ? item.vehicle.prixBase * (1 - getDiscount(String(item.vehicle.id)) / 100)
        : item.vehicle.prixBase;
      return sum + (vehiclePrice + optionsTotal) * item.quantity;
    }, 0);
  };

  const adjustedTotal = calculateTotal();
  const taxAmount = adjustedTotal * taxRate;
  const totalWithTax = adjustedTotal + taxAmount;

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency', currency: 'EUR', maximumFractionDigits: 0
    }).format(price);
  };

  // --- ACTION DE COMMANDE ---
  const handlePurchase = async () => {
    const storedUser = localStorage.getItem('user');
    if (!storedUser) {
      toast.error("Veuillez vous connecter pour finaliser votre achat");
      navigate('/login');
      return;
    }

    let user;
    try {
      user = JSON.parse(storedUser);
    } catch (error) {
      console.error("Erreur parsing user from localStorage:", error);
      toast.error("Erreur lors de la lecture des données utilisateur");
      return;
    }

    // Vérifier et normaliser les données
    console.log('=== HANDLEURCHASE DEBUG ===');
    console.log('Raw storedUser string:', storedUser);
    console.log('Utilisateur depuis localStorage (parsed):', user);
    console.log('User.id:', user.id);
    console.log('User.clientId:', user.clientId);
    console.log('User.userId:', user.userId);
    console.log('Toutes les clés user:', Object.keys(user));

    // Déterminer le clientId - essayer plusieurs champs
    let clientId = user.id || user.clientId || user.userId;
    
    console.log('ClientId déterminé:', clientId);
    console.log('ClientId type:', typeof clientId);
    
    if (!clientId) {
      console.error('ClientId not found in user object:', user);
      toast.error("ID client manquant. Veuillez vous reconnecter.");
      localStorage.removeItem('user');
      navigate('/login');
      return;
    }

    if (items.length === 0) {
      toast.error("Votre panier est vide");
      return;
    }

    try {
      setIsSubmitting(true);

      // Vérifier les IDs des véhicules
      const vehicleIds = items.map(item => item.vehicle.id);
      console.log('Vehicle IDs avant envoi:', vehicleIds);
      console.log('Vehicle IDs types:', vehicleIds.map(id => typeof id));

      // 1. Création de la commande initiale
      const orderPayload = {
        clientId: Number(clientId), // S'assurer que c'est un nombre
        vehiculesIds: vehicleIds.map(id => Number(id)), // S'assurer que tous les IDs sont des nombres
        typePaiement: paymentMethod,
        paysLivraison: country
      };

      console.log('Payload de commande final avant envoi:', JSON.stringify(orderPayload));

      const response = await commandeApi.creer(orderPayload);
      const orderId = response.data.id;

      console.log('Commande créée avec ID:', orderId);

      // 2. Validation immédiate (Génère la liasse)
      const validationResponse = await commandeApi.valider(orderId);
      
      console.log('=== VALIDATION RESPONSE ===');
      console.log('Type de la réponse:', typeof validationResponse.data);
      console.log('Réponse complète:', validationResponse.data);
      
      // La réponse peut être une string ou un objet
      let orderData: any = {
        id: orderId,
        typePaiement: paymentMethod,
        paysLivraison: country,
        total: adjustedTotal,
        taxAmount: taxAmount
      };

      // Si la réponse est un objet (pas une string), utiliser les données
      if (typeof validationResponse.data === 'object' && validationResponse.data !== null) {
        orderData = { ...orderData, ...validationResponse.data };
      }
      
      console.log('Données finales de la commande:', orderData);

      // 3. Charger la liasse de documents
      console.log('Chargement de la liasse...');
      try {
        const liasseResponse = await liasseApi.getLiasseVierge();
        console.log('Liasse récupérée:', liasseResponse.data);
        
        if (liasseResponse.data.documents && Array.isArray(liasseResponse.data.documents)) {
          const processedDocs = liasseResponse.data.documents.map((doc: any, index: number) => ({
            id: doc.id || `doc-${index}`,
            name: doc.titre || 'Document sans titre',
            type: doc.type || 'document',
            content: doc.contenu || '',
            dateCreation: doc.dateCreation || new Date().toISOString()
          }));
          
          setLiasseDocuments(processedDocs);
          orderData.documents = processedDocs;
          console.log('Documents traités et ajoutés à la commande:', processedDocs);
        }
      } catch (liasseError) {
        console.error('Erreur au chargement de la liasse:', liasseError);
        // Continuer même si la liasse échoue - ce n'est pas bloquant
      }

      // Stocker les montants pour affichage
      setOrderTotal(orderData.total || adjustedTotal);
      setOrderTaxAmount(orderData.taxAmount || taxAmount);
      
      setCreatedOrder(orderData);
      setShowOrderSuccess(true);
      clearCart();
      toast.success("Commande enregistrée et liasse de documents générée !");
    } catch (error) {
      console.error("Erreur commande:", error);
      toast.error("Erreur lors de la création de la commande.");
    } finally {
      setIsSubmitting(false);
    }
  };

  if (items.length === 0 && !showOrderSuccess) {
    return (
      <div className="min-h-screen bg-[#050505] flex flex-col">
        <Navbar />
        <main className="flex-1 flex flex-col items-center justify-center p-6 text-center">
          <div className="w-24 h-24 bg-white/5 rounded-full flex items-center justify-center mb-6">
            <ShoppingBag className="w-10 h-10 text-white/20" />
          </div>
          <h1 className="text-3xl font-display font-bold text-white mb-2">Panier vide</h1>
          <p className="text-white/40 mb-8 max-w-xs">Votre garage est encore vide. Explorez nos modèles pour commencer.</p>
          <Link to="/catalogue"><Button variant="hero" size="lg">Voir le catalogue</Button></Link>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#050505] text-white">
      <Navbar />
      <main className="pt-32 pb-24 container max-w-7xl mx-auto px-6">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-16">
          
          {/* LISTE DES ARTICLES */}
          <div className="lg:col-span-2 space-y-8">
            <div className="flex items-center justify-between border-b border-white/5 pb-6">
              <h1 className="text-4xl font-display font-bold">Panier</h1>
              {canUndo && (
                <Button variant="ghost" size="sm" onClick={undo} className="text-primary hover:bg-primary/10">
                  <Undo2 className="w-4 h-4 mr-2"/> Restaurer
                </Button>
              )}
            </div>

            {items.map((item) => (
              <div key={item.vehicle.id} className="group bg-white/[0.02] border border-white/5 rounded-2xl p-6 transition-all hover:border-white/10">
                <div className="flex flex-col md:flex-row gap-8">
                  {/* Image */}
                  <div className="w-full md:w-48 aspect-[4/3] bg-white/5 rounded-xl overflow-hidden flex-shrink-0">
                    <img 
                      src={item.vehicle.imageLink ? `http://10.17.211.91:8000${item.vehicle.imageLink}` : "/placeholder-car.png"}
                      className="w-full h-full object-cover"
                      alt={item.vehicle.modele}
                    />
                  </div>

                  {/* Infos */}
                  <div className="flex-1">
                    <div className="flex justify-between">
                      <div>
                        <p className="text-primary font-bold text-[10px] uppercase tracking-widest">{item.vehicle.marque}</p>
                        <h3 className="text-2xl font-bold">{item.vehicle.modele}</h3>
                      </div>
                      <Button variant="ghost" size="icon" onClick={() => removeFromCart(item.vehicle.id)} className="text-white/20 hover:text-red-500">
                        <Trash2 className="w-5 h-5" />
                      </Button>
                    </div>

                    <div className="flex items-center gap-8 mt-6">
                      <div className="flex items-center gap-4 bg-white/5 border border-white/5 rounded-full px-4 py-2">
                        <button onClick={() => updateQuantity(item.vehicle.id, item.quantity - 1)} className="hover:text-primary"><Minus className="w-4 h-4"/></button>
                        <span className="font-mono font-bold w-4 text-center">{item.quantity}</span>
                        <button onClick={() => updateQuantity(item.vehicle.id, item.quantity + 1)} className="hover:text-primary"><Plus className="w-4 h-4"/></button>
                      </div>
                      <p className="text-2xl font-display font-bold">{formatPrice(item.vehicle.prixBase * item.quantity)}</p>
                    </div>
                  </div>
                </div>

                {/* OPTIONS BACKEND */}
                <div className="mt-8 pt-8 border-t border-white/5">
                  <div className="flex items-center gap-2 mb-4">
                    <Settings2 className="w-3.5 h-3.5 text-primary" />
                    <span className="text-[10px] font-bold uppercase tracking-widest text-white/40">Personnalisation</span>
                  </div>
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                    {availableOptions.map((option) => {
                      const isSelected = item.options.some(opt => opt.id === option.id);
                      return (
                        <div 
                          key={option.id}
                          onClick={() => isSelected ? removeOption(item.vehicle.id, option.id) : addOption(item.vehicle.id, option)}
                          className={cn(
                            "flex items-center gap-3 p-3 rounded-xl border transition-all cursor-pointer select-none",
                            isSelected ? "border-primary bg-primary/5 text-white" : "border-white/5 text-white/40 hover:border-white/20"
                          )}
                        >
                          <Checkbox checked={isSelected} className="border-white/20 data-[state=checked]:bg-primary" />
                          <div className="min-w-0">
                            <p className="text-xs font-bold truncate">{option.nom}</p>
                            <p className="text-[10px] font-medium text-primary">+{formatPrice(option.prix)}</p>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* RÉCAPITULATIF COMMANDE */}
          <div className="lg:col-span-1">
            <div className="bg-white/[0.03] border border-white/10 rounded-3xl p-8 sticky top-32 backdrop-blur-xl">
              <h2 className="text-xl font-bold mb-8">Détails de facturation</h2>
              
              <div className="space-y-6">
                <div>
                  <label className="text-[10px] font-bold uppercase text-white/40 mb-3 block">Pays de livraison</label>
                  <Select value={country} onValueChange={setCountry}>
                    <SelectTrigger className="bg-white/5 border-white/5 h-12 rounded-xl focus:ring-primary">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent className="bg-[#0a0a0a] border-white/10 text-white">
                      {Object.keys(taxRates).map(c => <SelectItem key={c} value={c}>{c}</SelectItem>)}
                    </SelectContent>
                  </Select>
                </div>

                <div>
                  <label className="text-[10px] font-bold uppercase text-white/40 mb-3 block">Méthode de règlement</label>
                  <div className="grid grid-cols-2 gap-3">
                    <button 
                      onClick={() => setPaymentMethod('COMPTANT')}
                      className={cn(
                        "flex flex-col items-center justify-center p-4 rounded-2xl border transition-all gap-2",
                        paymentMethod === 'COMPTANT' ? "border-primary bg-primary/5 text-white" : "border-white/5 text-white/20"
                      )}
                    >
                      <CreditCard className="w-5 h-5" />
                      <span className="text-[10px] font-bold">COMPTANT</span>
                    </button>
                    <button 
                      onClick={() => setPaymentMethod('CREDIT')}
                      className={cn(
                        "flex flex-col items-center justify-center p-4 rounded-2xl border transition-all gap-2",
                        paymentMethod === 'CREDIT' ? "border-primary bg-primary/5 text-white" : "border-white/5 text-white/20"
                      )}
                    >
                      <Landmark className="w-5 h-5" />
                      <span className="text-[10px] font-bold">CRÉDIT</span>
                    </button>
                  </div>
                </div>
              </div>

              <div className="mt-10 pt-8 border-t border-white/10 space-y-4">
                <div className="flex justify-between text-sm">
                  <span className="text-white/40">Sous-total</span>
                  <span className="font-mono">{formatPrice(adjustedTotal)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-white/40">TVA ({(taxRate * 100)}%)</span>
                  <span className="font-mono">{formatPrice(taxAmount)}</span>
                </div>
                <div className="flex justify-between items-end pt-4">
                  <span className="text-lg font-bold">Total TTC</span>
                  <span className="text-3xl font-display font-bold text-primary">{formatPrice(totalWithTax)}</span>
                </div>
              </div>

              <Button 
                onClick={handlePurchase} 
                disabled={isSubmitting}
                className="w-full h-16 rounded-2xl mt-8 text-lg font-bold group"
                variant="hero"
              >
                {isSubmitting ? (
                  <Loader2 className="w-6 h-6 animate-spin" />
                ) : (
                  <>
                    {paymentMethod === 'COMPTANT' ? 'Confirmer l\'achat' : 'Soumettre le dossier'}
                    <ShoppingBag className="w-5 h-5 ml-2 group-hover:translate-x-1 transition-transform" />
                  </>
                )}
              </Button>
              <p className="text-[9px] text-white/20 text-center mt-4 uppercase tracking-widest">Transaction sécurisée par protocole SSL</p>
            </div>
          </div>
        </div>
      </main>
      <Footer />

      <OrderSuccessModal 
        order={createdOrder}
        isOpen={showOrderSuccess}
        onClose={() => setShowOrderSuccess(false)}
        total={orderTotal}
        taxAmount={orderTaxAmount}
        documents={liasseDocuments}
      />
    </div>
  );
};

export default Cart;