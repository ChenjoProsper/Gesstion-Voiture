package com.gestion_voiture.gestionnaire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_voiture.gestionnaire.models.LiasseDocuments;

@Repository
public interface LiasseDocumentsRepository extends JpaRepository<LiasseDocuments,Long> {
    
}
