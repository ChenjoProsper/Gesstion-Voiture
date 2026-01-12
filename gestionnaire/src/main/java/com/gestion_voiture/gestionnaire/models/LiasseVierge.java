package com.gestion_voiture.gestionnaire.models;

import java.util.ArrayList;
import java.util.List;

public final class LiasseVierge {
    private static LiasseVierge instance = null; 
    private List<String> documents;

    private LiasseVierge() {
        documents = new ArrayList<>();
        documents.add("Demande d'immatriculation");
        documents.add("Certificat de cession");
        documents.add("Bon de commande");
    }

    public static LiasseVierge getInstance() {
        if (instance == null) {
            instance = new LiasseVierge();
        }
        return instance;
    }

    public List<String> getDocuments() {
        return documents;
    }
}