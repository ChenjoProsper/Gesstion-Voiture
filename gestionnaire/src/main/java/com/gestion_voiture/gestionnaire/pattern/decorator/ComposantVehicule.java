package com.gestion_voiture.gestionnaire.pattern.decorator;

import org.springframework.stereotype.Component;

@Component
public interface ComposantVehicule {
    String getDescription();
    Double calculePrix();
}