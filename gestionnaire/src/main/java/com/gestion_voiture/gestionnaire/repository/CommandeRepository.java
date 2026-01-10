package com.gestion_voiture.gestionnaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande,Long> {

}
