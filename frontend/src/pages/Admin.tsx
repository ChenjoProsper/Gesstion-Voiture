import React, { useState, useEffect } from 'react';
import Navbar from '@/components/layout/Navbar';
import Footer from '@/components/layout/Footer';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useAuth } from '@/context/AuthContext';
import { useStock } from '@/context/StockContext';
import { vehiculeApi, stockApi, optionApi } from '@/service/api';
import { 
  Settings, Tag, Car, AlertTriangle, Undo2, 
  Plus, Upload, X, Loader2, Bike, Info, List
} from 'lucide-react';
import { cn } from '@/lib/utils';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

const Admin: React.FC = () => {
  const { isAdmin } = useAuth();
  const { canUndo } = useStock();

  // --- États pour les données ---
  const [soldableVehicles, setSoldableVehicles] = useState<any[]>([]);
  const [options, setOptions] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  
  // --- États pour les formulaires ---
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
  const [isOptionDialogOpen, setIsOptionDialogOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [singleImageBase64, setSingleImageBase64] = useState<string>("");
  const [salePercentages, setSalePercentages] = useState<Record<number, number>>({});

  const [category, setCategory] = useState<'AUTO' | 'SCOOTER'>('AUTO');
  const [engine, setEngine] = useState<'ELECTRIQUE' | 'ESSENCE'>('ELECTRIQUE');

  const [formData, setFormData] = useState({
    reference: '', marque: '', modele: '', prixBase: 0,
    cylindree: 0, nombrePortes: 4, espaceCoffre: 400, description: ''
  });

  const [optionData, setOptionData] = useState({ nom: '', prix: 0 });

  const fetchData = async () => {
    try {
      setLoading(true);
      const [vRes, oRes] = await Promise.all([
        vehiculeApi.getVehiculesSoldables(),
        optionApi.lister()
      ]);
      setSoldableVehicles(Array.isArray(vRes.data) ? vRes.data : []);
      setOptions(Array.isArray(oRes.data) ? oRes.data : []);
    } catch (error) {
      console.error("Erreur chargement:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isAdmin) fetchData();
  }, [isAdmin]);

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(price || 0);
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => setSingleImageBase64(reader.result as string);
      reader.readAsDataURL(file);
    }
  };

  const handleAddVehicle = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      // On retire le préfixe data:image/xxx;base64, pour le backend
      const cleanBase64 = singleImageBase64.includes(',') ? singleImageBase64.split(',')[1] : singleImageBase64;
      
      const payload = { 
        ...formData, 
        typeVehicule: `${category}_${engine}`, 
        imageData: cleanBase64,
        imageLink: "" 
      };
      await vehiculeApi.ajouter(payload);
      setIsAddDialogOpen(false);
      setSingleImageBase64("");
      fetchData();
    } catch (error) {
      alert("Erreur lors de la création du véhicule");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCreateOption = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await optionApi.creer({ ...optionData, id: 0 });
      setIsOptionDialogOpen(false);
      setOptionData({ nom: '', prix: 0 });
      fetchData();
    } catch (error) {
      alert("Erreur lors de la création de l'option");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSolder = async (id: number) => {
    const percent = salePercentages[id] || 0;
    if (percent <= 0 || percent > 45) {
      alert("Le pourcentage doit être entre 1% et 45%");
      return;
    }
    try {
      await vehiculeApi.solderVehicule(id, percent);
      fetchData();
    } catch (error) {
      alert("Erreur lors de la mise en solde");
    }
  };

  if (!isAdmin) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <main className="pt-32 pb-24 text-center">
          <AlertTriangle className="w-16 h-16 text-destructive mx-auto mb-6" />
          <h1 className="text-3xl font-bold mb-4">Accès réservé</h1>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50/50">
      <Navbar />
      <main className="pt-32 pb-24">
        <div className="container px-4">
          
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-8">
            <div>
              <h1 className="text-3xl font-bold text-slate-900 flex items-center gap-3">
                <Settings className="w-8 h-8 text-blue-600" />
                Administration Centrale
              </h1>
            </div>

            <div className="flex gap-3">
              <Dialog open={isOptionDialogOpen} onOpenChange={setIsOptionDialogOpen}>
                <DialogTrigger asChild>
                  <Button variant="outline" className="h-12 rounded-xl">
                    <List className="w-5 h-5 mr-2" /> Nouvelle Option
                  </Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader><DialogTitle>Créer une option</DialogTitle></DialogHeader>
                  <form onSubmit={handleCreateOption} className="space-y-4 pt-4">
                    <div className="space-y-2">
                      <Label>Nom de l'option</Label>
                      <Input required value={optionData.nom} onChange={e => setOptionData({...optionData, nom: e.target.value})} />
                    </div>
                    <div className="space-y-2">
                      <Label>Prix (€)</Label>
                      <Input type="number" required value={optionData.prix} onChange={e => setOptionData({...optionData, prix: Number(e.target.value)})} />
                    </div>
                    <Button type="submit" className="w-full bg-blue-600" disabled={isSubmitting}>Enregistrer l'option</Button>
                  </form>
                </DialogContent>
              </Dialog>

              <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
                <DialogTrigger asChild>
                  <Button className="bg-blue-600 hover:bg-blue-700 h-12 px-6 rounded-xl shadow-lg">
                    <Plus className="w-5 h-5 mr-2" /> Nouveau Véhicule
                  </Button>
                </DialogTrigger>
                <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
                  <DialogHeader><DialogTitle>Ajouter au catalogue</DialogTitle></DialogHeader>
                  <form onSubmit={handleAddVehicle} className="space-y-6 pt-4">
                    <div className="grid grid-cols-2 gap-4 p-4 bg-blue-50/50 rounded-xl border border-blue-100">
                      <div className="space-y-2">
                        <Label>Catégorie</Label>
                        <Select value={category} onValueChange={(v: any) => setCategory(v)}>
                          <SelectTrigger className="bg-white"><SelectValue /></SelectTrigger>
                          <SelectContent>
                            <SelectItem value="AUTO">Automobile</SelectItem>
                            <SelectItem value="SCOOTER">Scooter</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="space-y-2">
                        <Label>Moteur</Label>
                        <Select value={engine} onValueChange={(v: any) => setEngine(v)}>
                          <SelectTrigger className="bg-white"><SelectValue /></SelectTrigger>
                          <SelectContent>
                            <SelectItem value="ELECTRIQUE">Électrique</SelectItem>
                            <SelectItem value="ESSENCE">Essence</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <Input placeholder="Marque" required onChange={e => setFormData({...formData, marque: e.target.value})} />
                      <Input placeholder="Modèle" required onChange={e => setFormData({...formData, modele: e.target.value})} />
                      <Input placeholder="Référence (VIN)" required onChange={e => setFormData({...formData, reference: e.target.value})} />
                      <Input type="number" placeholder="Prix de base" required onChange={e => setFormData({...formData, prixBase: Number(e.target.value)})} />
                    </div>
                    <div className="space-y-2">
                      <Label>Image du véhicule</Label>
                      <div className="flex items-center gap-4">
                        {singleImageBase64 ? (
                          <div className="relative w-24 h-24 rounded border overflow-hidden">
                            <img src={singleImageBase64} className="object-cover w-full h-full" alt="Preview" />
                            <button type="button" onClick={() => setSingleImageBase64("")} className="absolute top-0 right-0 bg-red-500 text-white p-1"><X className="w-3 h-3" /></button>
                          </div>
                        ) : (
                          <label className="w-24 h-24 border-2 border-dashed rounded flex flex-col items-center justify-center cursor-pointer">
                            <Upload className="w-5 h-5 text-slate-400" />
                            <input type="file" hidden onChange={handleImageChange} accept="image/*" />
                          </label>
                        )}
                      </div>
                    </div>
                    <Button type="submit" className="w-full bg-slate-900" disabled={isSubmitting}>Lancer la production</Button>
                  </form>
                </DialogContent>
              </Dialog>
            </div>
          </div>

          <Tabs defaultValue="vehicles" className="space-y-6">
            <TabsList className="bg-white border rounded-xl p-1">
              <TabsTrigger value="vehicles" className="rounded-lg px-8">Véhicules ({soldableVehicles.length})</TabsTrigger>
              <TabsTrigger value="options" className="rounded-lg px-8">Options ({options.length})</TabsTrigger>
            </TabsList>

            <TabsContent value="vehicles">
              <div className="bg-white rounded-3xl shadow-sm border overflow-hidden">
                <div className="p-6 border-b bg-slate-50/50 flex justify-between items-center">
                  <h2 className="font-bold text-xl">Catalogue en stock</h2>
                  {canUndo && (
                    <Button onClick={() => stockApi.annulerDernierSolde().then(fetchData)} variant="outline" size="sm" className="text-amber-600 border-amber-200">
                      <Undo2 className="w-4 h-4 mr-2" /> Annuler dernier solde massif
                    </Button>
                  )}
                </div>
                <div className="divide-y">
                  {loading ? <div className="p-10 text-center"><Loader2 className="animate-spin mx-auto" /></div> :
                  soldableVehicles.map((v) => (
                    <div key={v.id} className="p-6 flex items-center justify-between hover:bg-slate-50">
                      <div className="flex items-center gap-6">
                        <div className="w-24 h-16 bg-slate-100 rounded-lg flex items-center justify-center border overflow-hidden">
                          {v.imageData ? <img src={v.imageData.startsWith('data') ? v.imageData : `data:image/png;base64,${v.imageData}`} className="object-cover h-full w-full" alt="" /> : <Car className="text-slate-300" />}
                        </div>
                        <div>
                          <div className="flex items-center gap-2">
                            <h3 className="font-bold">{v.marque} {v.modele}</h3>
                            <Badge variant="outline">{v.typeVehicule}</Badge>
                          </div>
                          <p className="text-sm text-slate-500 font-mono">{v.reference}</p>
                        </div>
                      </div>
                      <div className="flex items-center gap-4">
                        <div className="text-right mr-4">
                          <p className="text-lg font-black">{formatPrice(v.prixBase)}</p>
                        </div>
                        <div className="flex items-center gap-2 bg-slate-100 p-1.5 rounded-xl border">
                          <Input 
                            type="number" 
                            placeholder="%" 
                            className="w-16 h-9 bg-white"
                            max={45}
                            value={salePercentages[v.id] || ''}
                            onChange={e => setSalePercentages({...salePercentages, [v.id]: Number(e.target.value)})}
                          />
                          <Button 
                            size="sm" 
                            className="bg-emerald-600 h-9"
                            onClick={() => handleSolder(v.id)}
                          >
                            <Tag className="w-4 h-4 mr-2" /> Solder
                          </Button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </TabsContent>

            <TabsContent value="options">
              <div className="bg-white rounded-3xl shadow-sm border overflow-hidden">
                <div className="p-6 border-b bg-slate-50/50">
                  <h2 className="font-bold text-xl">Options disponibles</h2>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 p-6">
                  {options.map((opt) => (
                    <div key={opt.id} className="p-4 border rounded-2xl flex justify-between items-center bg-slate-50">
                      <div>
                        <p className="font-bold text-slate-900">{opt.nom}</p>
                        <p className="text-xs text-slate-500 italic">ID: {opt.id}</p>
                      </div>
                      <Badge className="bg-blue-100 text-blue-700 hover:bg-blue-100">
                        +{formatPrice(opt.prix)}
                      </Badge>
                    </div>
                  ))}
                </div>
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default Admin;