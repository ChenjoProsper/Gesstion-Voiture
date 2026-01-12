package com.gestion_voiture.gestionnaire.services.impl;

import com.gestion_voiture.gestionnaire.models.Document;
import com.gestion_voiture.gestionnaire.models.DocumentPdfAdapter;
import com.gestion_voiture.gestionnaire.services.DocumentService;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Override
    public String genererEtImprimerPdf(String contenu) {
        Document doc = new DocumentPdfAdapter();
        doc.setContenu(contenu);
        doc.dessiner();
        doc.imprimer();
        return "Document PDF généré et imprimé avec succès.";
    }
}