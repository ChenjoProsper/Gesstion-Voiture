package com.gestion_voiture.gestionnaire.services.impl;

import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;
import com.gestion_voiture.gestionnaire.mapper.CommandeMapper;
import com.gestion_voiture.gestionnaire.models.Client;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.Option;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.models.Enum.EtatCommande;
import com.gestion_voiture.gestionnaire.pattern.decorator.ComposantVehicule;
import com.gestion_voiture.gestionnaire.pattern.decorator.OptionDecorateur;
import com.gestion_voiture.gestionnaire.repository.ClientRepository;
import com.gestion_voiture.gestionnaire.repository.CommandeRepository;
import com.gestion_voiture.gestionnaire.repository.OptionRepository;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.services.CommandeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ServiceLiasse serviceLiasse;
    private final CommandeMapper commandeMapper;
    private final OptionRepository optionRepository;

    @Override
    @Transactional
    public CommandeResultDTO passerCommande(CommandeDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        List<Vehicule> vehicules = vehiculeRepository.findAllById(dto.getVehiculesIds());

        Commande commande = commandeMapper.toEntity(dto);
        commande.setClient(client);
        commande.setVehicules(vehicules);
        commande.setDateCommande(LocalDateTime.now());

        Commande savedCommande = commandeRepository.save(commande);
        
        return commandeMapper.toDto(savedCommande);
    }

    @Override
    @Transactional
    public Double calculerPrixFinal(Long commandeId, Map<Long, List<Long>> optionsParVehicule) {

        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        double total = 0.0;

        for (Vehicule vehicule : commande.getVehicules()) {
            ComposantVehicule vehiculeCalcule = vehicule;

            List<Long> optionsIds = optionsParVehicule.get(vehicule.getId());
            if (optionsIds != null) {
                List<Option> options = optionRepository.findAllById(optionsIds);
                for (Option opt : options) {
                    vehiculeCalcule = new OptionDecorateur(vehiculeCalcule, opt);
                }
            }

            total += vehiculeCalcule.calculePrix();
        }

        return total;
    }


    @Override
    @Transactional
    public void validerCommande(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        commande.ajouterObservateur(serviceLiasse);

        commande.setEtat(EtatCommande.VALIDE);

        commandeRepository.save(commande);
    }
}