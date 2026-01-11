package com.gestion_voiture.gestionnaire.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gestion_voiture.gestionnaire.models.Enum.EtatCommande;
import com.gestion_voiture.gestionnaire.pattern.observer.CommandeSujet;
import com.gestion_voiture.gestionnaire.pattern.observer.Observateur;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Commande implements CommandeSujet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateCommande;

    private String paysLivraison;

    @Enumerated(EnumType.STRING)
    private EtatCommande etat = EtatCommande.EN_COURS;
    
    private Double montantTotal;

    @ManyToOne
    private Client client;

    @ManyToMany
    @JoinTable(
        name = "commande_vehicules",
        joinColumns = @JoinColumn(name = "commande_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicule_id")
    )
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

    @Transient
    private List<Observateur> observateurs = new ArrayList<>();

    @Override
    public void ajouterObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void retirerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for (Observateur o : observateurs) {
            o.actualiser(this);
        }
    }

    @Override
    public String getEtatNom() {
        return this.getEtat() != null ? this.getEtat().name() : "INCONNU";
    }

    public void setEtat(EtatCommande etat) {
        this.etat = etat;
        if (etat == EtatCommande.VALIDE) {
            notifierObservateurs();
        }
    }
    public double calculeMontantFinal(double sommeVehiculesDecores) {
        
        double montant = sommeVehiculesDecores;
        
        montant = appliqueFraisSpecifiques(montant);
        
        return montant;
    }
}