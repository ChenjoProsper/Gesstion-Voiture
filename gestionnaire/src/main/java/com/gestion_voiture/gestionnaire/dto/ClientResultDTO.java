package com.gestion_voiture.gestionnaire.dto;

import lombok.Data;

@Data
public class ClientResultDTO extends ClientDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
}
