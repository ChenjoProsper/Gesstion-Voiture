package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;

@Entity
public class AutoElectrique extends Automobile {
    @Override
    public Double calculePrix() {
        return getPrixBase() * 1.2; 
    }

    @Override
    public String getDescription() {
        return String.format("Automobile Électrique %s %s - %d portes. Prix de base: %.2f FCFA", 
                getMarque(), getModele(), getNombrePortes(), getPrixBase());
    }
}