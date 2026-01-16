package com.gestion_voiture.gestionnaire.dto;

import lombok.Data;

@Data
public class SoldeRequestDTO {
    private Double pourcentage;
    private String marque; // Optionnel : pour ne solder qu'une marque précise
}