package com.gestion_voiture.gestionnaire.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.Panier;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {
    Optional<Panier> findByClientId(Long clientId);
}
