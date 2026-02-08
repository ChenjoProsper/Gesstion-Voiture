package com.gestion_voiture.gestionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.models.AutoElectrique;
import com.gestion_voiture.gestionnaire.models.AutoEssence;
import com.gestion_voiture.gestionnaire.models.Automobile;
import com.gestion_voiture.gestionnaire.models.Scooter;
import com.gestion_voiture.gestionnaire.models.ScooterElectrique;
import com.gestion_voiture.gestionnaire.models.ScooterEssence;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    List<Vehicule> findByMarque(String marque);

    @Query("SELECT v FROM Vehicule v WHERE " +
            "LOWER(v.marque) LIKE CONCAT('%', :mot, '%') OR " +
            "LOWER(v.modele) LIKE CONCAT('%', :mot, '%') OR " +
            "LOWER(v.reference) LIKE CONCAT('%', :mot, '%')")
    List<Vehicule> searchByKeyword(@Param("mot") String mot);

    @Query("SELECT a FROM AutoEssence a")
    List<AutoEssence> findAllAutoEssence();

    @Query("SELECT a FROM AutoElectrique a")
    List<AutoElectrique> findAllAutoElectrique();

    @Query("SELECT s FROM ScooterEssence s")
    List<ScooterEssence> findAllScooterEssence();

    @Query("SELECT s FROM ScooterElectrique s")
    List<ScooterElectrique> findAllScooterElectrique();
}
