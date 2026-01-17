package com.gestion_voiture.gestionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.Automobile;

@Repository
public interface AutomobileRepository extends JpaRepository<Automobile, Long> {
    /**
     * Recherche les automobiles par marque
     */
    List<Automobile> findByMarque(String marque);
    
    /**
     * Recherche les automobiles par modèle
     */
    List<Automobile> findByModele(String modele);
    
    /**
     * Recherche les automobiles par marque et modèle
     */
    List<Automobile> findByMarqueAndModele(String marque, String modele);
    
    /**
     * Recherche par plage de prix
     */
    List<Automobile> findByPrixBaseBetween(Double prixMin, Double prixMax);
    
    /**
     * Recherche par marque contenant (insensible à la casse)
     */
    List<Automobile> findByMarqueIgnoreCaseContaining(String marque);
    
    /**
     * Recherche par modèle contenant (insensible à la casse)
     */
    List<Automobile> findByModeleIgnoreCaseContaining(String modele);
}
