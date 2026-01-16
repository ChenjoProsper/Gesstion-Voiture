package com.gestion_voiture.gestionnaire.services;

import org.springframework.stereotype.Service;

@Service
public interface FormulaireService {  
    String genererFormulaire(String typeRendu);
}