package com.gestion_voiture.gestionnaire.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class LiasseDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    public void ajouteDocument(Document doc) {
        this.documents.add(doc);
    }

    public Boolean supprimerDocument(Document doc){
        return this.documents.remove(doc);
    }

    public List<Document>listerDocuments(){
        return this.documents;
    }
}
