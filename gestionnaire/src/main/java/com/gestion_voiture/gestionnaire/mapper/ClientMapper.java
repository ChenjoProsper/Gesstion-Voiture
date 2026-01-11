package com.gestion_voiture.gestionnaire.mapper;

import com.gestion_voiture.gestionnaire.dto.ClientDTO;
import com.gestion_voiture.gestionnaire.dto.ClientResultDTO;
import com.gestion_voiture.gestionnaire.models.Client;
import com.gestion_voiture.gestionnaire.models.ClientParticulier;
import com.gestion_voiture.gestionnaire.models.Societe;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResultDTO toDto(Client client){
        ClientResultDTO clientResultDTO = new ClientResultDTO();
        clientResultDTO.setEmail(client.getEmail());
        clientResultDTO.setNom(client.getNom());
        clientResultDTO.setTelephone(client.getTelephone());
        clientResultDTO.setId(client.getId());

        return clientResultDTO;
    }

    public Client toEntity(ClientDTO dto) {
        if (dto == null) return null;

        Client client = dto.isSociete() ? new Societe() : new ClientParticulier();
        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());
        client.setTelephone(dto.getTelephone());
        return client;
    }
}