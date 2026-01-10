package com.gestion_voiture.gestionnaire.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Societe extends Client {
    @OneToMany(cascade = CascadeType.ALL)
    private List<Client> filiales = new ArrayList<>();

    @Override
    public void ajouteFiliale(Client filiale) {
        this.filiales.add(filiale);
    }
}
