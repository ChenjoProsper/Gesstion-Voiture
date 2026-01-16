package com.gestion_voiture.gestionnaire.models;

public abstract class Formulaire {
    protected FormulaireImpl impl;

    protected Formulaire(FormulaireImpl impl) {
        this.impl = impl;
    }

    public abstract void afficher();
}