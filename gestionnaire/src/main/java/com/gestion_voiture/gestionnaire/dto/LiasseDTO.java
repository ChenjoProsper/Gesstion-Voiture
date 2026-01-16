package com.gestion_voiture.gestionnaire.dto;

import java.util.List;
import lombok.Data;

@Data
public class LiasseDTO {
    private List<String> typesDocuments;
    private String description;
}