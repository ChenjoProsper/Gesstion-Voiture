package com.gestion_voiture.gestionnaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    
}
