package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;

@Entity
public class CertificatCession extends Document {
    @Override
    public String genereContenu() {
        return "Contenu du certificat de cession...";
    }
}