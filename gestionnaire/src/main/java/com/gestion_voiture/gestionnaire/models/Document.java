package com.gestion_voiture.gestionnaire.models;

<<<<<<< HEAD
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String contenu;
    private LocalDateTime dateCreation;

    public abstract String genereContenu();
=======
public interface Document {
    void setContenu(String contenu);
    String dessiner();
    void imprimer(); 
>>>>>>> origin/Beautrel
}