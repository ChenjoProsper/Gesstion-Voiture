package com.gestion_voiture.gestionnaire.dto;

import com.gestion_voiture.gestionnaire.models.Enum.TypeVehicule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehiculeDTO {
    private String reference;
    private String marque;
    private String modele;
    private Double prixBase;
    private Integer cylindree;
    private Integer nombrePortes;
    private Double espaceCoffre;
    private String imageData; // Image en base64 lors de la création
    private String imageLink; // Lien/URL vers l'image (retourné par le serveur)
    @NotNull
    private TypeVehicule typeVehicule;
}