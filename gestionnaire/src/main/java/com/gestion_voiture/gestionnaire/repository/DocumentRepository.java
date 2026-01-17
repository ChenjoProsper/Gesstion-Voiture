package com.gestion_voiture.gestionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    
    /**
     * Récupère tous les documents d'un client
     */
    List<Document> findByClientId(Long clientId);
    
    /**
     * Récupère les documents d'un client par titre
     */
    List<Document> findByClientIdAndTitreContaining(Long clientId, String titre);
}

