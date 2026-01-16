package com.gestion_voiture.gestionnaire.models;

public class DocumentPdfAdapter implements Document {
    private final ComposantPDF composantPdf;

    public DocumentPdfAdapter() {
        this.composantPdf = new ComposantPDF();
    }

    @Override
    public void setContenu(String contenu) {
        composantPdf.pdfFixerContenu(contenu);
    }

    @Override
    public String dessiner() {
        composantPdf.pdfPreparerAffichage();
        return "Rendu PDF terminé";
    }

    @Override
    public void imprimer() {
        composantPdf.pdfImprimer();
    }
}