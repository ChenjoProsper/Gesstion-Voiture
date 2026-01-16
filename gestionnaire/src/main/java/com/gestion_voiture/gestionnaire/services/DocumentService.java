package com.gestion_voiture.gestionnaire.services;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.DocumentHtmlDTO;
import com.gestion_voiture.gestionnaire.dto.DocumentPdfDTO;
import com.gestion_voiture.gestionnaire.models.Document;

@Service
public interface DocumentService {
    Document creerDocumentPdf(DocumentPdfDTO dto);

    Document creerDocumentHtml(DocumentHtmlDTO dto);
}