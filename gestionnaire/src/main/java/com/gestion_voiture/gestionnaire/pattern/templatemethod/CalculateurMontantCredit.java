package com.gestion_voiture.gestionnaire.pattern.templatemethod;

import org.springframework.stereotype.Component;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.CommandeCredit;

/**
 * Implémentation Template Method pour les commandes à crédit
 */
@Component
public class CalculateurMontantCredit extends CalculateurMontantCommande {
    
    @Override
    protected Double appliquerRemises(Double montantBase, Commande commande) {
        // Pas de remise pour les commandes à crédit
        return montantBase;
    }
    
    @Override
    protected Double appliquerFraisSpecifiques(Double montant, Commande commande) {
        // Appliquer les frais d'intérêt de crédit
        if (commande instanceof CommandeCredit credit) {
            Double tauxInteret = credit.getTauxInteret() != null ? credit.getTauxInteret() : 0.0;
            return montant * (1 + (tauxInteret / 100.0));
        }
        return montant;
    }
    
    @Override
    protected Double appliquerFraisLivraison(Double montant, Commande commande) {
        // Frais de livraison progressifs pour crédit (1% du montant)
        return montant + (montant * 0.01);
    }
}
