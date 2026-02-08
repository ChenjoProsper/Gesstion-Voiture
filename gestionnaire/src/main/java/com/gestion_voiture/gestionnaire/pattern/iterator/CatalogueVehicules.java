package com.gestion_voiture.gestionnaire.pattern.iterator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.gestion_voiture.gestionnaire.models.Vehicule;


@Component
public class CatalogueVehicules implements VehiculeCatalogue {
    private List<Vehicule> vehicules = new ArrayList<>();
    
    @Override
    public VehiculeIterator createIterator() {
        return new VehiculeIteratorImpl(new ArrayList<>(vehicules));
    }
    
    @Override
    public void addVehicule(Vehicule vehicule) {
        vehicules.add(vehicule);
    }
    
    @Override
    public void removeVehicule(Vehicule vehicule) {
        vehicules.remove(vehicule);
    }
    
    @Override
    public List<Vehicule> getVehicules() {
        return new ArrayList<>(vehicules);
    }
    
    /**
     * Retourne le nombre de véhicules dans le catalogue
     */
    public int size() {
        return vehicules.size();
    }
}
