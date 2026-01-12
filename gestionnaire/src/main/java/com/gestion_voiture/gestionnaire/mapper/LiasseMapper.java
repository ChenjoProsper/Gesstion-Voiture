package com.gestion_voiture.gestionnaire.mapper;

import com.gestion_voiture.gestionnaire.dto.LiasseDTO;
import com.gestion_voiture.gestionnaire.models.LiasseVierge;
import org.springframework.stereotype.Component;

@Component
public class LiasseMapper {
    public LiasseDTO toDTO(LiasseVierge model) {
        LiasseDTO dto = new LiasseDTO();
        dto.setTypesDocuments(model.getDocuments());
        dto.setDescription("Gabarit de liasse de documents vierge");
        return dto;
    }
}