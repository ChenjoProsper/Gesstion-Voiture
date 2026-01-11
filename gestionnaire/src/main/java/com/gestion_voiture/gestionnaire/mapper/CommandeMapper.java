package com.gestion_voiture.gestionnaire.mapper;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.CommandeCredit;
import com.gestion_voiture.gestionnaire.models.Enum.TypePaiement;
import com.gestion_voiture.gestionnaire.pattern.factory.CommandeComptantCreator;
import com.gestion_voiture.gestionnaire.pattern.factory.CommandeCreditCreator;
import com.gestion_voiture.gestionnaire.services.ClientService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommandeMapper {

    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final CommandeCreditCreator commandeCreditCreator;
    private final CommandeComptantCreator commandeComptantCreator;

    public CommandeResultDTO toDto(Commande commande) {

        CommandeResultDTO dto = new CommandeResultDTO();
        dto.setId(commande.getId());
        dto.setEtat(commande.getEtat().name());
        dto.setDateCommande(commande.getDateCommande());
        dto.setClient(clientMapper.toDto(commande.getClient()));
        dto.setPaysLivraison(commande.getPaysLivraison());
        if (commande instanceof CommandeCredit credit) {
            dto.setTypePaiement(TypePaiement.CREDIT);
            dto.setTauxInteret(credit.getTauxInteret());
        } else {
            dto.setTypePaiement(TypePaiement.COMPTANT);
        }

        return dto;
    }

    public Commande toEntity(CommandeDTO dto) {

        Commande commande = switch (dto.getTypePaiement()) {

            case CREDIT -> commandeCreditCreator.creerCommande();

            case COMPTANT -> commandeComptantCreator.creerCommande();
        };

        commande.setClient(clientService.findById(dto.getClientId()));
        commande.setPaysLivraison(dto.getPaysLivraison());

        return commande;
    }
}
