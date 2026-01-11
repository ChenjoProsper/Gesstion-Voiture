package com.gestion_voiture.gestionnaire.services;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;

@Service
public interface CommandeService {
    
    public CommandeResultDTO passerCommande(CommandeDTO dto);
    public Double calculerPrixFinal(Long commandeId);
}
