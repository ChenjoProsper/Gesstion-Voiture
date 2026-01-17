package com.gestion_voiture.gestionnaire.pattern.iterator;

import java.util.List;
import com.gestion_voiture.gestionnaire.models.Vehicule;

/**
 * Interface Iterable pour créer des itérateurs de véhicules
 */
public interface VehiculeCatalogue {
    /**
     * Crée un itérateur pour parcourir les véhicules
     */
    VehiculeIterator createIterator();
    
    /**
     * Ajoute un véhicule au catalogue
     */
    void addVehicule(Vehicule vehicule);
    
    /**
     * Retire un véhicule du catalogue
     */
    void removeVehicule(Vehicule vehicule);
    
    /**
     * Retourne la liste des véhicules
     */
    List<Vehicule> getVehicules();
}
