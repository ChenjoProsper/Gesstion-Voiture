package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;

@Entity
public class DemandeImmatriculation extends Document {
    @Override
    public String genereContenu() {
        return "Contenu du formulaire d'immatriculation...";
    }
}