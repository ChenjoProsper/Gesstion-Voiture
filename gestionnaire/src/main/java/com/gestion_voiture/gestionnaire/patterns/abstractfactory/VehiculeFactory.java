package com.gestion_voiture.gestionnaire.patterns.abstractfactory;

import com.gestion_voiture.gestionnaire.models.*;

public interface VehiculeFactory {
    Automobile creerAutomobile();
    Scooter creerScooter();
}
