package com.gestion_voiture.gestionnaire.pattern.templatemethod;

import com.gestion_voiture.gestionnaire.models.Commande;


public abstract class CalculateurMontantCommande {
    
    /**
     * Méthode template qui définit l'algorithme de calcul du montant
     */
    public final Double calculerMontantFinal(Commande commande) {
        // Étape 1: Calculer le montant de base des véhicules
        Double montantBase = calculerMontantBase(commande);
        
        // Étape 2: Appliquer les remises
        Double montantApresRemise = appliquerRemises(montantBase, commande);
        
        // Étape 3: Appliquer les frais spécifiques
        Double montantAvecFrais = appliquerFraisSpecifiques(montantApresRemise, commande);
        
        // Étape 4: Calculer les taxes
        Double montantAvecTaxes = appliquerTaxes(montantAvecFrais, commande);
        
        // Étape 5: Appliquer les frais de livraison
        Double montantFinal = appliquerFraisLivraison(montantAvecTaxes, commande);
        
        return montantFinal;
    }
    
    /**
     * Étape 1 : Calculer le montant de base
     * (Implémentation par défaut : somme des prix des véhicules)
     */
    protected Double calculerMontantBase(Commande commande) {
        return commande.getVehicules().stream()
                .mapToDouble(v -> v.calculePrix())
                .sum();
    }
    
    /**
     * Étape 2 : Appliquer les remises 
     */
    protected abstract Double appliquerRemises(Double montantBase, Commande commande);
    
    /**
     * Étape 3 : Appliquer les frais spécifiques 
     */
    protected abstract Double appliquerFraisSpecifiques(Double montant, Commande commande);
    
    /**
     * Étape 4 : Calculer les taxes selon le pays
     */
    protected Double appliquerTaxes(Double montant, Commande commande) {
        if ("Cameroun".equals(commande.getPaysLivraison())) {
            return montant * 1.192; // 19.2% de TVA
        } else {
            return montant * 1.20; // 20% de TVA par défaut
        }
    }
    
    /**
     * Étape 5 : Appliquer les frais de livraison (à surcharger)
     */
    protected Double appliquerFraisLivraison(Double montant, Commande commande) {
        // Implémentation par défaut : pas de frais supplémentaires
        return montant;
    }
}
