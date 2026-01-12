package com.gestion_voiture.gestionnaire.services.impl;

import com.gestion_voiture.gestionnaire.dto.LiasseDTO;
import com.gestion_voiture.gestionnaire.mapper.LiasseMapper;
import com.gestion_voiture.gestionnaire.models.LiasseVierge;
import com.gestion_voiture.gestionnaire.services.LiasseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiasseServiceImpl implements LiasseService {
    private final LiasseMapper liasseMapper;

    @Override
    public LiasseDTO obtenirLiasseVierge() {
        return liasseMapper.toDTO(LiasseVierge.getInstance());
    }
}