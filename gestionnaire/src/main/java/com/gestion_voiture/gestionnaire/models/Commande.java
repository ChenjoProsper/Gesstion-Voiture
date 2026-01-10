package com.gestion_voiture.gestionnaire.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateCommande;

    private String paysLivraison;

    @Enumerated(EnumType.STRING)
    private EtatCommande etat;
    
    private Double montantTotal;

    @ManyToOne
    private Client client;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Vehicule> vehicules = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private LiasseDocuments liasseDocuments;

    public final Double calculeMontantTotal() {
            Double montantBase = vehicules.stream()
                                        .mapToDouble(Vehicule::calculePrix)
                                        .sum();
            return appliqueFraisSpecifiques(montantBase) + calculeTaxes(montantBase);
    }

    private Double calculeTaxes(Double montant) {
        return "Cameroun".equals(paysLivraison) ? montant * 0.192 : montant * 0.20;
    }

    protected abstract Double appliqueFraisSpecifiques(Double montant);

    public void ajouterVehicule(Vehicule v) {
        this.vehicules.add(v);
    }

    public void supprimerVehicule(Vehicule v) {
        this.vehicules.remove(v);
    }
}