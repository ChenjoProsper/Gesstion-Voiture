package com.gestion_voiture.gestionnaire.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.gestion_voiture.gestionnaire.models.Enum.TypePaiement;

import lombok.Data;

@Data
public class CommandeResultDTO {
    private Long id;
    private LocalDateTime dateCommande;
    private String etat;
    private Double montantTotal;
    private String paysLivraison;
    private TypePaiement typePaiement;
    private Double tauxInteret;
    private ClientResultDTO client;
    private List<VehiculeResultDTO> vehicules;
}