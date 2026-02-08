package com.gestion_voiture.gestionnaire.services.impl;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.FormulaireRequestDTO;
import com.gestion_voiture.gestionnaire.models.Formulaire;
import com.gestion_voiture.gestionnaire.models.FormulaireImmatriculation;
import com.gestion_voiture.gestionnaire.pattern.bridge.FormulaireHtmlImpl;
import com.gestion_voiture.gestionnaire.pattern.bridge.FormulaireWidgetImpl;
import com.gestion_voiture.gestionnaire.services.FormulaireService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormulaireServiceImpl implements FormulaireService {

    private final FormulaireHtmlImpl htmlImpl;
    private final FormulaireWidgetImpl widgetImpl;

    @Override
    public String genererFormulaire(FormulaireRequestDTO dto) {
        
        var impl = dto.getFormat().equalsIgnoreCase("html") ? htmlImpl : widgetImpl;

        Formulaire formulaire;
        if (dto.getTypeFormulaire().equalsIgnoreCase("immatriculation")) {
            formulaire = new FormulaireImmatriculation(impl);
        } else {
            return "Type de formulaire inconnu";
        }

        formulaire.afficher();
        return "Formulaire " + dto.getTypeFormulaire() + " généré au format " + dto.getFormat();
    }
}