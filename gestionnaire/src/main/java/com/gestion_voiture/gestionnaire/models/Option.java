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

    public Option() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public boolean estCompatible(Option autreOption) {
        if (autreOption == null) return true;
        if (this.nom == null || autreOption.nom == null) return true;

        String a = this.nom.toLowerCase();
        String b = autreOption.nom.toLowerCase();

        // Exemples d'incompatibilités connues
        if ((a.contains("sport") && b.contains("cuir")) || (a.contains("cuir") && b.contains("sport"))) {
            return false;
        }
        if ((a.contains("clim") && b.contains("chauffage")) || (a.contains("chauffage") && b.contains("clim"))) {
            return false;
        }

        // Par défaut, considérer compatibles. Pour règles complexes, utiliser une table dédiée.
        return true;
    }
}
