package com.gestion_voiture.gestionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private Long clientId;
    private String nom;
    private String email;
    private boolean authenticated;
    private String message;
}
