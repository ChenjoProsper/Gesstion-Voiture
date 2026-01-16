import React, { useState } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Order, OrderDocument } from '@/types/vehicle';
import { CheckCircle2, FileText, Download, Eye, Car } from 'lucide-react';
import { cn } from '@/lib/utils';
import { motion, AnimatePresence } from 'framer-motion';

interface OrderSuccessModalProps {
  order: Order | null;
  isOpen: boolean;
  onClose: () => void;
  total?: number | null;
  taxAmount?: number | null;
  documents?: any[];
}

type ViewFormat = 'html' | 'pdf';

const OrderSuccessModal: React.FC<OrderSuccessModalProps> = ({ order, isOpen, onClose, total, taxAmount, documents = [] }) => {
  const [selectedDoc, setSelectedDoc] = useState<any>(null);
  const [viewFormat, setViewFormat] = useState<ViewFormat>('html');

  if (!order) return null;

  const formatPrice = (price: number | undefined | null) => {
    if (price === undefined || price === null || isNaN(price)) {
      return '-- €';
    }
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR',
      maximumFractionDigits: 0
    }).format(price);
  };

  const getDocIcon = (type: string) => {
    switch (type) {
      case 'immatriculation':
        return Car;
      case 'cession':
        return FileText;
      default:
        return FileText;
    }
  };

  const renderDocumentPreview = () => {
    if (!selectedDoc) return null;

    return (
      <motion.div 
        className="mt-6 p-6 bg-secondary rounded-2xl"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.3 }}
      >
        <div className="flex items-center justify-between mb-4">
          <h4 className="font-medium text-foreground">{selectedDoc.name}</h4>
          <div className="flex gap-2">
            <Button
              variant={viewFormat === 'html' ? 'default' : 'outline'}
              size="sm"
              onClick={() => setViewFormat('html')}
              className="rounded-lg"
            >
              HTML
            </Button>
            <Button
              variant={viewFormat === 'pdf' ? 'default' : 'outline'}
              size="sm"
              onClick={() => setViewFormat('pdf')}
              className="rounded-lg"
            >
              PDF
            </Button>
          </div>
        </div>

        {viewFormat === 'html' ? (
          <div className="prose max-w-none text-sm bg-card p-4 rounded-xl border border-border">
            {selectedDoc.content ? (
              <div className="whitespace-pre-wrap text-muted-foreground">
                {selectedDoc.content}
              </div>
            ) : (
              <p className="text-muted-foreground">[Contenu du document {selectedDoc.name}]</p>
            )}
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center py-12 bg-card rounded-xl border border-border">
            <FileText className="w-16 h-16 text-primary/30 mb-4" />
            <p className="text-muted-foreground mb-4">Aperçu PDF simulé</p>
            <Button variant="hero" size="sm" className="rounded-xl">
              <Download className="w-4 h-4 mr-2" />
              Télécharger le PDF
            </Button>
          </div>
        )}
      </motion.div>
    );
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto rounded-3xl border-0 shadow-soft-lg">
        <DialogHeader>
          <div className="flex items-center gap-4 mb-4">
            <motion.div 
              className="w-16 h-16 rounded-full bg-green-100 flex items-center justify-center"
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ type: "spring", duration: 0.5 }}
            >
              <CheckCircle2 className="w-8 h-8 text-green-600" />
            </motion.div>
            <div>
              <DialogTitle className="text-2xl font-display">
                Commande confirmée !
              </DialogTitle>
              <p className="text-muted-foreground mt-1">
                Commande N° {order.id}
              </p>
            </div>
          </div>
        </DialogHeader>

        {/* Order Summary */}
        <div className="bg-secondary rounded-2xl p-5 mb-6">
          <div className="flex items-center justify-between text-sm">
            <span className="text-muted-foreground">Total TTC</span>
            <span className="text-xl font-bold text-gradient">
              {total !== null && total !== undefined 
                ? formatPrice(total) 
                : formatPrice((order?.total || 0) + (order?.taxAmount || 0))}
            </span>
          </div>
          <div className="flex items-center justify-between text-sm mt-2">
            <span className="text-muted-foreground">Mode de paiement</span>
            <Badge className="bg-accent text-accent-foreground border-0">
              {order?.typePaiement === 'COMPTANT' || order?.paymentMethod === 'comptant' ? 'Paiement comptant' : 'Demande de crédit'}
            </Badge>
          </div>
        </div>

        {/* Documents Section - Floating paper effect */}
        <div>
          <h3 className="text-lg font-display font-bold text-foreground mb-4">
            Liasse de documents générée
          </h3>
          <p className="text-sm text-muted-foreground mb-4">
            {documents && documents.length > 0
              ? `Votre liasse contient ${documents.length} document(s). Cliquez sur un document pour le visualiser.`
              : 'Votre liasse contient 3 documents obligatoires. Cliquez sur un document pour le visualiser.'}
          </p>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {documents && documents.length > 0 ? (
              documents.map((doc, index) => {
                const Icon = getDocIcon(doc.type);
                const isSelected = selectedDoc?.id === doc.id;

                return (
                  <motion.button
                    key={doc.id}
                    onClick={() => setSelectedDoc(isSelected ? null : doc)}
                    className={cn(
                      "p-5 rounded-2xl text-left transition-all group relative overflow-hidden",
                      "bg-card shadow-card",
                      isSelected && "ring-2 ring-primary shadow-violet"
                    )}
                    initial={{ opacity: 0, y: 20, rotate: -2 }}
                    animate={{ 
                      opacity: 1, 
                      y: 0, 
                      rotate: isSelected ? 0 : (index % 2 === 0 ? -1 : 1)
                    }}
                    whileHover={{ y: -4, rotate: 0 }}
                    transition={{ delay: index * 0.1, duration: 0.3 }}
                  >
                    <div className="flex items-center gap-3 mb-3">
                      <div className={cn(
                        "w-10 h-10 rounded-xl flex items-center justify-center transition-colors",
                        isSelected ? "bg-primary/10" : "bg-accent"
                      )}>
                        <Icon className="w-5 h-5 text-primary" />
                      </div>
                    </div>
                    <h4 className="font-medium text-foreground text-sm mb-1">
                      {doc.name}
                    </h4>
                    <p className="text-xs text-muted-foreground flex items-center gap-1">
                      <Eye className="w-3 h-3" />
                      Voir le document
                    </p>
                  </motion.button>
                );
              })
            ) : order.documents && order.documents.length > 0 ? (
              order.documents.map((doc, index) => {
                const Icon = getDocIcon(doc.type);
                const isSelected = selectedDoc?.id === doc.id;

                return (
                  <motion.button
                    key={doc.id}
                    onClick={() => setSelectedDoc(isSelected ? null : doc)}
                    className={cn(
                      "p-5 rounded-2xl text-left transition-all group relative overflow-hidden",
                      "bg-card shadow-card",
                      isSelected && "ring-2 ring-primary shadow-violet"
                    )}
                    initial={{ opacity: 0, y: 20, rotate: -2 }}
                    animate={{ 
                      opacity: 1, 
                      y: 0, 
                      rotate: isSelected ? 0 : (index % 2 === 0 ? -1 : 1)
                    }}
                    whileHover={{ y: -4, rotate: 0 }}
                    transition={{ delay: index * 0.1, duration: 0.3 }}
                  >
                    <div className="flex items-center gap-3 mb-3">
                      <div className={cn(
                        "w-10 h-10 rounded-xl flex items-center justify-center transition-colors",
                        isSelected ? "bg-primary/10" : "bg-accent"
                      )}>
                        <Icon className="w-5 h-5 text-primary" />
                      </div>
                    </div>
                    <h4 className="font-medium text-foreground text-sm mb-1">
                      {doc.name}
                    </h4>
                    <p className="text-xs text-muted-foreground flex items-center gap-1">
                      <Eye className="w-3 h-3" />
                      Voir le document
                    </p>
                  </motion.button>
                );
              })
            ) : (
              <div className="col-span-3 p-6 rounded-2xl bg-card text-center">
                <FileText className="w-8 h-8 text-muted-foreground mx-auto mb-2" />
                <p className="text-sm text-muted-foreground">
                  Les documents seront générés et disponibles prochainement.
                </p>
              </div>
            )}
          </div>

          <AnimatePresence>
            {renderDocumentPreview()}
          </AnimatePresence>
        </div>

        {/* Close Button */}
        <div className="flex justify-end mt-6 pt-6 border-t border-border">
          <Button variant="hero" onClick={onClose} className="rounded-xl">
            Terminer
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default OrderSuccessModal;
