package com.gestion_voiture.gestionnaire.pattern.bridge;

import org.springframework.stereotype.Component;

@Component
public class FormulaireHtmlImpl implements FormulaireImpl {
    @Override
    public void dessinerTexte(String texte) {
        System.out.println("<label>" + texte + "</label><input type='text' />");
    }

    @Override
    public String collecterSaisie() {
        return "[Données saisies en HTML]";
    }
}