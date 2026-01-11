package com.gestion_voiture.gestionnaire.pattern.abstractfactory;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.models.*;

@Component
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
