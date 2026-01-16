package com.gestion_voiture.gestionnaire.services.impl;

import com.gestion_voiture.gestionnaire.models.*;
import com.gestion_voiture.gestionnaire.services.FormulaireService;
import org.springframework.stereotype.Service;

@Service
public class FormulaireServiceImpl implements FormulaireService {

    @Override
    public String genererFormulaire(String typeRendu) {
        FormulaireImpl rendu;

        if ("html".equalsIgnoreCase(typeRendu)) {
            rendu = new FormHTMLImpl();
        } else {
            rendu = new FormWidgetImpl();
        }

        Formulaire form = new FormulaireImmatriculation(rendu);
        
        form.afficher();

        return "Formulaire d'immatriculation généré avec succès en mode : " + typeRendu.toUpperCase();
    }
}