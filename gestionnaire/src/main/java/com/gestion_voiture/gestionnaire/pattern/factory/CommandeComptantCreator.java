package com.gestion_voiture.gestionnaire.pattern.factory;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.CommandeComptant;

@Component
public class CommandeComptantCreator extends CommandeCreator {
    @Override
    public Commande creerCommande() {
        return new CommandeComptant();
    }
}
