import React, { useState, useEffect } from 'react';
import Navbar from '@/components/layout/Navbar';
import Footer from '@/components/layout/Footer';
import { Button } from '@/components/ui/button';
import { useAuth } from '@/context/AuthContext';
import { FileText, Download, Eye, FileCheck, Car, Loader2, RefreshCw } from 'lucide-react';
import { cn } from '@/lib/utils';
import { liasseApi, documentsApi } from '@/service/api';
import { toast } from 'sonner';

type DocumentType = 'immatriculation' | 'cession' | 'commande';
type ViewFormat = 'html' | 'pdf';

interface DocumentItem {
  id: string;
  type: DocumentType;
  name: string;
  description: string;
  icon: React.ElementType;
  titre?: string;
  contenu?: string;
  dateCreation?: string;
}

const defaultDocuments: DocumentItem[] = [
  {
    id: 'immatriculation',
    type: 'immatriculation',
    name: "Demande d'immatriculation",
    description: "Formulaire officiel de demande d'immatriculation du véhicule.",
    icon: Car
  },
  {
    id: 'cession',
    type: 'cession',
    name: 'Certificat de cession',
    description: "Document attestant le transfert de propriété du véhicule.",
    icon: FileCheck
  },
  {
    id: 'commande',
    type: 'commande',
    name: 'Bon de commande',
    description: "Récapitulatif détaillé de votre commande.",
    icon: FileText
  }
];

const Documents: React.FC = () => {
  const { isAuthenticated } = useAuth();
  const [selectedDocIndex, setSelectedDocIndex] = useState<number | null>(null);
  const [viewFormat, setViewFormat] = useState<ViewFormat>('html');
  const [liasseDocuments, setLiasseDocuments] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  // Charger la liasse au montage
  useEffect(() => {
    if (isAuthenticated) {
      loadLiasse();
    }
  }, [isAuthenticated]);

  const loadLiasse = async () => {
    setIsLoading(true);
    try {
      const response = await liasseApi.getLiasseVierge();
      console.log('Réponse API liasse:', response.data);
      
      if (response.data.documents && Array.isArray(response.data.documents)) {
        const processedDocs = response.data.documents.map((doc: any, index: number) => ({
          id: doc.id || `doc-${index}`,
          titre: doc.titre || 'Document sans titre',
          contenu: doc.contenu || '',
          dateCreation: doc.dateCreation || new Date().toISOString(),
          type: doc.type || 'document'
        }));
        
        setLiasseDocuments(processedDocs);
        if (processedDocs.length > 0) {
          setSelectedDocIndex(0);
          toast.success(`${processedDocs.length} document(s) chargé(s)`);
        } else {
          setSelectedDocIndex(null);
          toast.info('Aucun document disponible');
        }
        console.log('Documents traités:', processedDocs);
      } else {
        setLiasseDocuments([]);
        setSelectedDocIndex(null);
        toast.info('Aucun document trouvé');
      }
    } catch (error) {
      console.error("Erreur lors du chargement de la liasse:", error);
      toast.error("Impossible de charger les documents");
      setLiasseDocuments([]);
      setSelectedDocIndex(null);
    } finally {
      setIsLoading(false);
    }
  };

  const generateFullLiasse = async () => {
    setIsLoading(true);
    try {
      // Recharger les documents après génération
      await loadLiasse();
      toast.success("Liasse générée avec succès!");
    } catch (error) {
      console.error("Erreur lors de la génération de la liasse:", error);
      toast.error("Erreur lors de la génération de la liasse");
    } finally {
      setIsLoading(false);
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <main className="pt-32 pb-24">
          <div className="container px-4 text-center">
            <FileText className="w-16 h-16 text-primary mx-auto mb-6" />
            <h1 className="text-3xl font-display font-bold text-foreground mb-4">
              Accès réservé
            </h1>
            <p className="text-muted-foreground">
              Veuillez vous connecter pour accéder à vos documents.
            </p>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  const renderDocumentPreview = () => {
    if (selectedDocIndex === null || !liasseDocuments[selectedDocIndex]) return null;
    
    const doc = liasseDocuments[selectedDocIndex];

    return (
      <div className="bg-card rounded-2xl border border-border p-8">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h3 className="text-xl font-display font-bold text-foreground">
              {doc.titre}
            </h3>
            <p className="text-sm text-muted-foreground mt-1">
              {new Date(doc.dateCreation).toLocaleDateString('fr-FR')}
            </p>
          </div>
          <div className="flex gap-2">
            <Button
              variant={viewFormat === 'html' ? 'default' : 'outline'}
              size="sm"
              onClick={() => setViewFormat('html')}
            >
              HTML
            </Button>
            <Button
              variant={viewFormat === 'pdf' ? 'default' : 'outline'}
              size="sm"
              onClick={() => setViewFormat('pdf')}
            >
              PDF
            </Button>
          </div>
        </div>

        <div className="bg-muted rounded-xl p-8 min-h-[400px] overflow-y-auto">
          {viewFormat === 'html' ? (
            <div className="prose prose-invert max-w-none text-sm">
              <div className="text-center mb-8">
                <Car className="w-12 h-12 text-primary mx-auto mb-4" />
                <h2 className="text-2xl font-display font-bold text-foreground">
                  AUTO-LUXE ÉLECTRIQUE
                </h2>
                <p className="text-muted-foreground">Véhicules Premium</p>
              </div>
              <hr className="border-border my-6" />
              <h3 className="text-xl font-display text-foreground">{doc.titre}</h3>
              <div className="bg-background/50 rounded-lg p-4 border border-border">
                <div className="text-sm text-muted-foreground whitespace-pre-wrap">
                  {doc.contenu || '[Contenu non disponible]'}
                </div>
              </div>
            </div>
          ) : (
            <div className="flex flex-col items-center justify-center h-full text-center">
              <FileText className="w-24 h-24 text-primary/30 mb-4" />
              <p className="text-muted-foreground mb-4">
                Aperçu PDF
              </p>
              <Button variant="hero">
                <Download className="w-4 h-4 mr-2" />
                Télécharger le PDF
              </Button>
            </div>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="pt-32 pb-24">
        <div className="container px-4">
          {/* Header */}
          <div className="mb-12">
            <p className="text-primary font-medium mb-2">Espace Personnel</p>
            <h1 className="text-4xl md:text-5xl font-display font-bold text-foreground mb-4">
              Mes <span className="text-gradient">Documents</span>
            </h1>
            <p className="text-muted-foreground max-w-2xl">
              Générez et consultez votre liasse de documents. 
              Disponible en format HTML ou PDF.
            </p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Document List */}
            <div className="space-y-4">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-lg font-display font-semibold text-foreground">
                  {liasseDocuments.length > 0 
                    ? `Mes Documents (${liasseDocuments.length})` 
                    : 'Documents'}
                </h2>
                {isLoading && <Loader2 className="w-4 h-4 animate-spin text-primary" />}
              </div>

              {liasseDocuments.length > 0 ? (
                <div className="space-y-3">
                  {liasseDocuments.map((doc, index) => (
                    <button
                      key={doc.id}
                      onClick={() => setSelectedDocIndex(index)}
                      className={cn(
                        "w-full p-4 rounded-xl border text-left transition-all group",
                        selectedDocIndex === index
                          ? "border-primary bg-primary/10"
                          : "border-border bg-card hover:border-primary/50"
                      )}
                    >
                      <div className="flex items-start gap-3">
                        <div className={cn(
                          "w-10 h-10 rounded-lg flex items-center justify-center flex-shrink-0 transition-colors",
                          selectedDocIndex === index 
                            ? "bg-primary/20" 
                            : "bg-muted group-hover:bg-primary/10"
                        )}>
                          <FileText className="w-5 h-5 text-primary" />
                        </div>
                        <div className="flex-1 min-w-0">
                          <h3 className="font-medium text-foreground truncate">
                            {doc.titre}
                          </h3>
                          <p className="text-xs text-muted-foreground mt-1">
                            {new Date(doc.dateCreation).toLocaleDateString('fr-FR')}
                          </p>
                        </div>
                      </div>
                    </button>
                  ))}
                </div>
              ) : (
                <div className="bg-card rounded-xl border border-border p-6 text-center">
                  <FileText className="w-8 h-8 text-muted-foreground mx-auto mb-3" />
                  <p className="text-sm text-muted-foreground mb-4">
                    Aucun document pour le moment
                  </p>
                  <Button 
                    variant="outline"
                    size="sm"
                    onClick={generateFullLiasse}
                    disabled={isLoading}
                  >
                    Générer la liasse
                  </Button>
                </div>
              )}

              {/* Generate All Button */}
              {liasseDocuments.length > 0 && (
                <Button 
                  variant="hero" 
                  className="w-full mt-6"
                  onClick={generateFullLiasse}
                  disabled={isLoading}
                >
                  {isLoading ? (
                    <>
                      <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                      Actualisation...
                    </>
                  ) : (
                    <>
                      <RefreshCw className="w-4 h-4 mr-2" />
                      Actualiser
                    </>
                  )}
                </Button>
              )}
            </div>

            {/* Preview Area */}
            <div className="lg:col-span-2">
              {selectedDocIndex !== null && liasseDocuments.length > 0 ? (
                renderDocumentPreview()
              ) : (
                <div className="bg-card rounded-2xl border border-border p-8 h-full flex flex-col items-center justify-center text-center">
                  <Eye className="w-16 h-16 text-muted-foreground mb-4" />
                  <h3 className="text-xl font-display font-semibold text-foreground mb-2">
                    {liasseDocuments.length === 0 ? 'Générez votre liasse' : 'Sélectionnez un document'}
                  </h3>
                  <p className="text-muted-foreground max-w-sm">
                    {liasseDocuments.length === 0
                      ? 'Créez une commande puis générez votre liasse de documents.'
                      : 'Cliquez sur un document pour afficher son contenu.'}
                  </p>
                </div>
              )}
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default Documents;
