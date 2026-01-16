package com.gestion_voiture.gestionnaire.models;

import com.gestion_voiture.gestionnaire.pattern.bridge.FormulaireImpl;

public class FormulaireImmatriculation extends Formulaire {

    public FormulaireImmatriculation(FormulaireImpl impl) {
        super(impl);
    }

    @Override
    public void afficher() {
        implementation.dessinerTexte("Numéro de Châssis");
        implementation.dessinerTexte("Nom du Propriétaire");
    }

    @Override
    public void generer() {
        System.out.println("Génération du formulaire d'immatriculation...");
    }
}