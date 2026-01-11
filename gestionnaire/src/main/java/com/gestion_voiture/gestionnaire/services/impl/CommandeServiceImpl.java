package com.gestion_voiture.gestionnaire.services.impl;

import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;
import com.gestion_voiture.gestionnaire.mapper.CommandeMapper;
import com.gestion_voiture.gestionnaire.models.Client;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.repository.ClientRepository;
import com.gestion_voiture.gestionnaire.repository.CommandeRepository;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.services.CommandeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final VehiculeRepository vehiculeRepository;
    private final CommandeMapper commandeMapper;

    @Override
    @Transactional
    public CommandeResultDTO passerCommande(CommandeDTO dto) {
        // 1. Récupérer le client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // 2. Récupérer les véhicules
        List<Vehicule> vehicules = vehiculeRepository.findAllById(dto.getVehiculesIds());

        // 3. Créer la commande (via le mapper pour le type de paiement)
        Commande commande = commandeMapper.toEntity(dto);
        commande.setClient(client);
        commande.setVehicules(vehicules);
        commande.setDateCommande(LocalDateTime.now());

        // 4. Sauvegarder (le montant total sera calculé à la volée par le modèle via Template Method)
        Commande savedCommande = commandeRepository.save(commande);
        
        return commandeMapper.toDto(savedCommande);
    }

    @Override
    public Double calculerPrixFinal(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
            .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        
        return commande.calculeMontantTotal();
    }
}