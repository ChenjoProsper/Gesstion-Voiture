package com.gestion_voiture.gestionnaire.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.dto.DocumentDTO;
import com.gestion_voiture.gestionnaire.dto.LiasseResultDTO;
import com.gestion_voiture.gestionnaire.models.Document;
import com.gestion_voiture.gestionnaire.models.LiasseDocuments;

@Component
public class LiasseMapper {

    public LiasseResultDTO toDto(LiasseDocuments liasse) {
        if (liasse == null)
            return null;
        LiasseResultDTO dto = new LiasseResultDTO();
        dto.setDocuments(liasse.listerDocuments().stream()
                .map(this::documentToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private DocumentDTO documentToDto(Document doc) {
        DocumentDTO dto = new DocumentDTO();
        dto.setTitre(doc.getTitre());
        dto.setContenu(doc.getContenu());
        dto.setDateCreation(doc.getDateCreation());
        return dto;
    }
}