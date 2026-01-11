package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;

@Entity
public class AutoEssence extends Automobile {
    @Override
    public Double calculePrix() {
        return getPrixBase() * 1.1;
    }

    @Override
    public String getDescription() {
        return String.format("Automobile Essence %s %s - %d portes. Prix de base: %.2f FCFA", 
                getMarque(), getModele(), getNombrePortes(), getPrixBase());
    }
}
