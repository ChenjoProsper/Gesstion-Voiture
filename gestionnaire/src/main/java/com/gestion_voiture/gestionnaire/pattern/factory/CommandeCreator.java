package com.gestion_voiture.gestionnaire.pattern.factory;

import com.gestion_voiture.gestionnaire.models.Commande;

public abstract class CommandeCreator {
    public abstract Commande creerCommande();
}
