package com.gestion_voiture.gestionnaire.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.dto.DocumentHtmlDTO;
import com.gestion_voiture.gestionnaire.dto.DocumentPdfDTO;
import com.gestion_voiture.gestionnaire.models.DocumentHtml;
import com.gestion_voiture.gestionnaire.models.DocumentPdf;

@Component
public class DocumentMapper {

    public DocumentPdf toEntity(DocumentPdfDTO dto) {
        if (dto == null)
            return null;
        DocumentPdf pdf = new DocumentPdf();
        pdf.setTitre(dto.getTitre());
        pdf.setContenu(dto.getContenu());
        pdf.setDateCreation(LocalDateTime.now());
        return pdf;
    }

    public DocumentHtml toEntity(DocumentHtmlDTO dto) {
        if (dto == null)
            return null;
        DocumentHtml html = new DocumentHtml();
        html.setTitre(dto.getTitre());
        html.setContenu(dto.getContenu());
        html.setDateCreation(LocalDateTime.now());
        return html;
    }
}