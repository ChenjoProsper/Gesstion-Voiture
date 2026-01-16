package com.gestion_voiture.gestionnaire.models;

import java.util.List;

import com.gestion_voiture.gestionnaire.pattern.decorator.ComposantVehicule;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehicule implements ComposantVehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String marque;
    private String modele;
    private Double prixBase;

    private String imageLink; // Lien/URL vers l'image du véhicule

    private Double pourcentageSolde; // Pourcentage de réduction appliqué

    @ManyToMany
    private List<Option> options;

    public abstract Double calculePrix();

    public abstract String getDescription();

    @ManyToMany(mappedBy = "vehicules")
    private List<Commande> commandes;

}