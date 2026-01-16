package com.gestion_voiture.gestionnaire.models;

import com.gestion_voiture.gestionnaire.pattern.bridge.FormulaireImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Formulaire {
    protected FormulaireImpl implementation;

    public abstract void afficher();

    public abstract void generer();
}