package com.gestion_voiture.gestionnaire.patterns.abstractfactory;

import com.gestion_voiture.gestionnaire.models.*;

public class FabriqueVehiculeElectrique implements VehiculeFactory {

    @Override
    public Automobile creerAutomobile() {
        return new AutoElectrique();
    }

    @Override
    public Scooter creerScooter() {
        return new ScooterElectrique();
    }
}
