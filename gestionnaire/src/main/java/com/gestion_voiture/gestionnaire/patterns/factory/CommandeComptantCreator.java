package com.gestion_voiture.gestionnaire.patterns.factory;

import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.CommandeComptant;

public class CommandeComptantCreator extends CommandeCreator {
    @Override
    public Commande creerCommande() {
        return new CommandeComptant();
    }
}
