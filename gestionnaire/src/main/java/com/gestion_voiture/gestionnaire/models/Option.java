package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private Double prix;

    public boolean estCompatible(Option autreOption) {
        // Logique de compatibilité entre options
        return true;
    }
}
