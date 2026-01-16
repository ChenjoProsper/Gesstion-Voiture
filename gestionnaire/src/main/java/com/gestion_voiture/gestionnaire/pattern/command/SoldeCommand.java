package com.gestion_voiture.gestionnaire.pattern.command;

import com.gestion_voiture.gestionnaire.models.Vehicule;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SoldeCommand implements Command {

    private final Vehicule vehicule;
    private final double pourcentageRemise;
    private double ancienPrixBase;

    public SoldeCommand(Vehicule vehicule, double pourcentageRemise) {
        this.vehicule = vehicule;
        this.pourcentageRemise = pourcentageRemise;
    }

    @Override
    public void execute() {
        this.ancienPrixBase = vehicule.getPrixBase();
        double nouveauPrix = ancienPrixBase * (1 - (pourcentageRemise / 100));
        vehicule.setPrixBase(nouveauPrix);
    }

    @Override
    public void undo() {
        vehicule.setPrixBase(ancienPrixBase);
    }
}