package com.gestion_voiture.gestionnaire.pattern.bridge;

import org.springframework.stereotype.Component;

@Component
public class FormulaireWidgetImpl implements FormulaireImpl {
    @Override
    public void dessinerTexte(String texte) {
        System.out.println("[Widget Label: " + texte + "] [Widget TextField]");
    }

    @Override
    public String collecterSaisie() {
        return "[Données saisies via Widgets]";
    }
}