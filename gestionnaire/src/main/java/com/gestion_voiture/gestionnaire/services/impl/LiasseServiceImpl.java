package com.gestion_voiture.gestionnaire.services.impl;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.LiasseResultDTO;
import com.gestion_voiture.gestionnaire.mapper.LiasseMapper;
import com.gestion_voiture.gestionnaire.pattern.singleton.LiasseVierge;
import com.gestion_voiture.gestionnaire.services.LiasseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiasseServiceImpl implements LiasseService {
    private final LiasseMapper liasseMapper;

    @Override
    public LiasseResultDTO getLiasseVierge() {
        return liasseMapper.toDto(LiasseVierge.getInstance());
    }
}