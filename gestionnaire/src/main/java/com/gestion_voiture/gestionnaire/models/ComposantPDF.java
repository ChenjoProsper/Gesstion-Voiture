package com.gestion_voiture.gestionnaire.models;

public class ComposantPDF {
    public void pdfFixerContenu(String contenu) {
        System.out.println("Contenu PDF : " + contenu);
    }
    public void pdfPreparerAffichage() {
        System.out.println("Préparation affichage PDF...");
    }
    public void pdfImprimer() { 
        System.out.println("Impression du flux PDF en cours...");
    }
}