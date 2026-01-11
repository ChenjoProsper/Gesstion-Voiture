package com.gestion_voiture.gestionnaire.pattern.decorator;

public abstract class VehiculeDecorateur implements ComposantVehicule {
    protected ComposantVehicule vehiculeDecore;

    public VehiculeDecorateur(ComposantVehicule vehiculeDecore) {
        this.vehiculeDecore = vehiculeDecore;
    }

    @Override
    public String getDescription() {
        return vehiculeDecore.getDescription();
    }

    @Override
    public Double calculePrix() {
        return vehiculeDecore.calculePrix();
    }
}