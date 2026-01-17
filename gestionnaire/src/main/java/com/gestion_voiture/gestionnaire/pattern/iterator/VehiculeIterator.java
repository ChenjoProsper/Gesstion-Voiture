package com.gestion_voiture.gestionnaire.pattern.iterator;

import java.util.Iterator;
import com.gestion_voiture.gestionnaire.models.Vehicule;

/**
 * Interface Iterator pour parcourir les véhicules du catalogue
 */
public interface VehiculeIterator extends Iterator<Vehicule> {
    /**
     * Retourne le prochain véhicule
     */
    @Override
    Vehicule next();
    
    /**
     * Vérifie s'il y a un véhicule suivant
     */
    @Override
    boolean hasNext();
}
