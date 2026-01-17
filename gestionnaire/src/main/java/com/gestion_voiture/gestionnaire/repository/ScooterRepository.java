package com.gestion_voiture.gestionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.Scooter;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, Long> {
    /**
     * Recherche les scooters par marque
     */
    List<Scooter> findByMarque(String marque);
    
    /**
     * Recherche les scooters par modèle
     */
    List<Scooter> findByModele(String modele);
    
    /**
     * Recherche les scooters par marque et modèle
     */
    List<Scooter> findByMarqueAndModele(String marque, String modele);
    
    /**
     * Recherche par plage de prix
     */
    List<Scooter> findByPrixBaseBetween(Double prixMin, Double prixMax);
    
    /**
     * Recherche par marque contenant (insensible à la casse)
     */
    List<Scooter> findByMarqueIgnoreCaseContaining(String marque);
    
    /**
     * Recherche par modèle contenant (insensible à la casse)
     */
    List<Scooter> findByModeleIgnoreCaseContaining(String modele);
    
    /**
     * Recherche par cylindrée
     */
    List<Scooter> findByCylindree(Integer cylindree);
}
