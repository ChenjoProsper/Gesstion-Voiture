package com.gestion_voiture.gestionnaire.dto;

import lombok.Data;

@Data
public class VehiculeResultDTO extends VehiculeDTO {
    private Long id;
    private String description;
}
