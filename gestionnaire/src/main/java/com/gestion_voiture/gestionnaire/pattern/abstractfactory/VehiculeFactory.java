package com.gestion_voiture.gestionnaire.pattern.abstractfactory;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.models.*;

@Component
public interface VehiculeFactory {
    Automobile creerAutomobile();
    Scooter creerScooter();
}
