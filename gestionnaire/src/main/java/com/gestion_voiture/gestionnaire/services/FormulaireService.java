package com.gestion_voiture.gestionnaire.services;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.FormulaireRequestDTO;

@Service
public interface FormulaireService {
    String genererFormulaire(FormulaireRequestDTO dto);
}