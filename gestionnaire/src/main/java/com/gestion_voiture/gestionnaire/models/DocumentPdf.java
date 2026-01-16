package com.gestion_voiture.gestionnaire.models;

import com.gestion_voiture.gestionnaire.pattern.adapter.PdfEngine;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentPdf extends Document {

    // On utilise Transient pour que Hibernate n'essaie pas de persister le moteur
    // PDF
    @Transient
    private final PdfEngine pdfEngine = new PdfEngine();

    public DocumentPdf() {
        super();
    }

    @Override
    public String genereContenu() {
        return pdfEngine.renderPdfData(this.getContenu());
    }
}