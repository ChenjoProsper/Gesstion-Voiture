package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class CommandeCredit extends Commande {
    private Double tauxInteret;

    @Override
    protected Double appliqueFraisSpecifiques(Double montant) {
        return montant * (1 + tauxInteret);
    }

    public void setTauxInteret(Double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }


}
