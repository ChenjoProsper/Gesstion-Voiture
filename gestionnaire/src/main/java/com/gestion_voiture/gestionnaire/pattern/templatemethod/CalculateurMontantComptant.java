package com.gestion_voiture.gestionnaire.pattern.templatemethod;

import org.springframework.stereotype.Component;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.CommandeComptant;

/**
 * Implémentation Template Method pour les commandes au comptant
 */
@Component
public class CalculateurMontantComptant extends CalculateurMontantCommande {
    
    @Override
    protected Double appliquerRemises(Double montantBase, Commande commande) {
        // Les commandes au comptant ont une remise de 5%
        if (commande instanceof CommandeComptant) {
            return montantBase * 0.95; // 5% de remise
        }
        return montantBase;
    }
    
    @Override
    protected Double appliquerFraisSpecifiques(Double montant, Commande commande) {
        // Pas de frais spécifiques pour comptant
        return montant;
    }
    
    @Override
    protected Double appliquerFraisLivraison(Double montant, Commande commande) {
        // Frais de livraison fixes de 50€ pour les commandes au comptant
        return montant + 50.0;
    }
}
