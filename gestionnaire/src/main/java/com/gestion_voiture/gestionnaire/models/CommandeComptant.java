package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;

@Entity
public class CommandeComptant extends Commande {
    @Override
    protected Double appliqueFraisSpecifiques(Double montant) {
        return montant;
    }
}
