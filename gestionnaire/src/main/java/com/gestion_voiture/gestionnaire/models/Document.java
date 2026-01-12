package com.gestion_voiture.gestionnaire.models;

public interface Document {
    void setContenu(String contenu);
    String dessiner();
    void imprimer(); 
}