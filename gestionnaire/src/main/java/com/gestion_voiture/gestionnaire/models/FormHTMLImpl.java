package com.gestion_voiture.gestionnaire.models;

public class FormHTMLImpl implements FormulaireImpl {
    @Override
    public void dessinerTexte(String texte) {
        System.out.println("<p>" + texte + "</p>");
    }
    @Override
    public void dessinerZoneSaisie(String label) {
        System.out.println("<label>" + label + "</label><input type='text'/>");
    }
}

public class FormWidgetImpl implements FormulaireImpl {
    @Override
    public void dessinerTexte(String texte) {
        System.out.println("[Widget-Label] " + texte);
    }
    @Override
    public void dessinerZoneSaisie(String label) {
        System.out.println("[Widget-TextField] pour " + label);
    }
}