package com.gestion_voiture.gestionnaire.services;

import com.gestion_voiture.gestionnaire.dto.LiasseDTO;

import org.springframework.stereotype.Service;

@Service
public interface LiasseService {
    LiasseDTO obtenirLiasseVierge();
}