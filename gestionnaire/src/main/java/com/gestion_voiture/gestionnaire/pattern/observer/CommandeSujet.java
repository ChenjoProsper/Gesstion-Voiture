package com.gestion_voiture.gestionnaire.pattern.observer;

import org.springframework.stereotype.Component;

@Component
public interface CommandeSujet {
    void ajouterObservateur(Observateur o);
    void retirerObservateur(Observateur o);
    void notifierObservateurs();
    String getEtatNom(); 
}