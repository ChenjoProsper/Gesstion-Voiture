package com.gestion_voiture.gestionnaire.pattern.observer;

public interface CommandeSujet {
    void ajouterObservateur(Observateur o);
    void retirerObservateur(Observateur o);
    void notifierObservateurs();
    String getEtatNom(); 
}