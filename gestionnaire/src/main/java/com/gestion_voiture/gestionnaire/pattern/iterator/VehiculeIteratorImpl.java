package com.gestion_voiture.gestionnaire.pattern.iterator;

import java.util.List;
import com.gestion_voiture.gestionnaire.models.Vehicule;

/**
 * Implémentation concrète de l'Iterator pour parcourir les véhicules
 */
public class VehiculeIteratorImpl implements VehiculeIterator {
    private List<Vehicule> vehicules;
    private int currentIndex = 0;
    
    public VehiculeIteratorImpl(List<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }
    
    @Override
    public boolean hasNext() {
        return currentIndex < vehicules.size();
    }
    
    @Override
    public Vehicule next() {
        if (!hasNext()) {
            throw new IllegalStateException("Pas de véhicule suivant");
        }
        return vehicules.get(currentIndex++);
    }
    
    /**
     * Réinitialise l'itérateur au début
     */
    public void reset() {
        currentIndex = 0;
    }
    
    /**
     * Retourne l'index courant
     */
    public int getCurrentIndex() {
        return currentIndex;
    }
}
