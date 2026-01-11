package com.gestion_voiture.gestionnaire.pattern.observer;

import org.springframework.stereotype.Component;

@Component
public interface Observateur {
    void actualiser(CommandeSujet sujet);
}

