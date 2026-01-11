package com.gestion_voiture.gestionnaire.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.services.ClientService;

import lombok.RequiredArgsConstructor;

import com.gestion_voiture.gestionnaire.dto.ClientDTO;
import com.gestion_voiture.gestionnaire.dto.ClientResultDTO;
import com.gestion_voiture.gestionnaire.mapper.ClientMapper;
import com.gestion_voiture.gestionnaire.models.Client;
import com.gestion_voiture.gestionnaire.models.Societe;
import com.gestion_voiture.gestionnaire.repository.ClientRepository;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResultDTO creerClient(ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);

    if (dto.getParentId() != null) {

        Client parent = clientRepository.findById(dto.getParentId())
                .orElseThrow(() -> new RuntimeException("Société mère introuvable"));

        parent.ajouteFiliale(client);
    
    }

        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public List<ClientResultDTO> listerTousLesClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                        .map(clientMapper::toDto)
                        .toList();
    }

    @Override
    public void ajouterFiliale(Long societeId, Long filialeId) {
        Societe societe = (Societe) clientRepository.findById(societeId)
                .orElseThrow(() -> new RuntimeException("Société mère non trouvée"));
        Client filiale = clientRepository.findById(filialeId)
                .orElseThrow(() -> new RuntimeException("Filiale non trouvée"));
        
        societe.ajouteFiliale(filiale); 
        clientRepository.save(societe);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
    }
}
