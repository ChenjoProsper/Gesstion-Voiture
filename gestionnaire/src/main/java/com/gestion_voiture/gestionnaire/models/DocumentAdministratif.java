package com.gestion_voiture.gestionnaire.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentAdministratif extends Document {

    public DocumentAdministratif() {
    }

    public DocumentAdministratif(String titre, String contenu) {
        this.setTitre(titre);
        this.setContenu(contenu);
        this.setDateCreation(LocalDateTime.now());
    }

    @Override
    public String genereContenu() {
        return "Contenu officiel du document : " + this.getTitre();
    }
}