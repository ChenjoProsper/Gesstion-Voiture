package com.gestion_voiture.gestionnaire.services;

import org.springframework.stereotype.Service;

@Service
public interface DocumentService {
    String genererEtImprimerPdf(String contenu);
}