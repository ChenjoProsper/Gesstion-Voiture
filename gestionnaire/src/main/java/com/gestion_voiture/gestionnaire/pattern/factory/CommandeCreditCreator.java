package com.gestion_voiture.gestionnaire.pattern.factory;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.CommandeCredit;

@Component
public class CommandeCreditCreator extends CommandeCreator {
    @Override
    public Commande creerCommande() {
        CommandeCredit c = new CommandeCredit();
        c.setTauxInteret(0.05);
        return c;
    }
}
