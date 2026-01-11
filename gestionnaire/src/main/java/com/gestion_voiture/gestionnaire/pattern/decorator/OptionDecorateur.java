package com.gestion_voiture.gestionnaire.pattern.decorator;

import com.gestion_voiture.gestionnaire.models.Option;

public class OptionDecorateur extends VehiculeDecorateur {
    private final Option option;

    public OptionDecorateur(ComposantVehicule vehiculeDecore, Option option) {
        super(vehiculeDecore);
        this.option = option;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Option: " + option.getNom();
    }

    @Override
    public Double calculePrix() {
        return super.calculePrix() + option.getPrix();
    }
}