package com.gestion_voiture.gestionnaire.pattern.iterator;

import java.util.List;
import com.gestion_voiture.gestionnaire.models.Vehicule;


public interface VehiculeCatalogue {
    
    VehiculeIterator createIterator();
    
  
    void addVehicule(Vehicule vehicule);
    
  
    void removeVehicule(Vehicule vehicule);
    
   
    List<Vehicule> getVehicules();
}
