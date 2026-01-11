package com.gestion_voiture.gestionnaire.dto;

import java.util.List;

import com.gestion_voiture.gestionnaire.models.Enum.TypePaiement;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommandeDTO {
    private Long clientId;
    private List<Long> vehiculesIds;

    @NotNull
    private TypePaiement typePaiement;

    private String paysLivraison;
}

