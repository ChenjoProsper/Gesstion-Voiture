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

    @NotNull
    private TypeVehicule typeVehicule;
}