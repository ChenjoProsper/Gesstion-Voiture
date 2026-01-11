package com.gestion_voiture.gestionnaire.pattern.factory;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.models.Commande;

@Component
public abstract class CommandeCreator {
    public abstract Commande creerCommande();
}
