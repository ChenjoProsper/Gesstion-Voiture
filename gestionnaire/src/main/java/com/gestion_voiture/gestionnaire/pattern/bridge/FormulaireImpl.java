package com.gestion_voiture.gestionnaire.pattern.bridge;

public interface FormulaireImpl {
    void dessinerTexte(String texte);

    String collecterSaisie();
}