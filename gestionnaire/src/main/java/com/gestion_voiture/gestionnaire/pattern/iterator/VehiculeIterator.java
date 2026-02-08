package com.gestion_voiture.gestionnaire.pattern.iterator;

import java.util.Iterator;
import com.gestion_voiture.gestionnaire.models.Vehicule;


public interface VehiculeIterator extends Iterator<Vehicule> {
   
    @Override
    Vehicule next();
    
   
    @Override
    boolean hasNext();
}
