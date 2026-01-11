package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;

@Entity
public class ScooterEssence extends Scooter {
    @Override
    public Double calculePrix() {
        return getPrixBase() * 1.05;
    }

    @Override
    public String getDescription() {
        return String.format("Scooter Essence %s %s - Cylindrée: %d cc. Prix de base: %.2f FCFA", 
                getMarque(), getModele(), getCylindree(), getPrixBase());
    }
}
