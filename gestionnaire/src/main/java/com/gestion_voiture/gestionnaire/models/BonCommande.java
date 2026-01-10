package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;

@Entity
public class BonCommande extends Document {
    @Override
    public String genereContenu() {
        return "Récapitulatif de la commande...";
    }
}