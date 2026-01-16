package com.gestion_voiture.gestionnaire.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DocumentDTO {
    private String titre;
    private String contenu;
    private LocalDateTime dateCreation;
}