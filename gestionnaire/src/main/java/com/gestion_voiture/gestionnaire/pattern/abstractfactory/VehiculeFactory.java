package com.gestion_voiture.gestionnaire.pattern.abstractfactory;

import com.gestion_voiture.gestionnaire.models.*;

public interface VehiculeFactory {
    Automobile creerAutomobile();
    Scooter creerScooter();
}
