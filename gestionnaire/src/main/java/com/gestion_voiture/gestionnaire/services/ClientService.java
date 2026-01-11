package com.gestion_voiture.gestionnaire.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.ClientDTO;
import com.gestion_voiture.gestionnaire.dto.ClientResultDTO;
import com.gestion_voiture.gestionnaire.models.Client;

@Service
public interface ClientService {
    
    public ClientResultDTO creerClient(ClientDTO dto);
    public List<ClientResultDTO> listerTousLesClients();
    public void ajouterFiliale(Long societeId, Long filialeId);
    public Client findById(Long id);
}
