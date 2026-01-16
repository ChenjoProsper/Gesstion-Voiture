package com.gestion_voiture.gestionnaire.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String nom;
    private String email;
    private String telephone;
    private String password;
    private String confirmPassword;
    private boolean isSociete;
    private Long parentId;
}
