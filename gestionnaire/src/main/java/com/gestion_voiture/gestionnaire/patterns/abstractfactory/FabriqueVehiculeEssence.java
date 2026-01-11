package com.gestion_voiture.gestionnaire.patterns.abstractfactory;

import com.gestion_voiture.gestionnaire.models.*;

public class FabriqueVehiculeEssence implements VehiculeFactory {

    @Override
    public Automobile creerAutomobile() {
        return new AutoEssence();
    }

    @Override
    public Scooter creerScooter() {
        return new ScooterEssence();
    }
}
