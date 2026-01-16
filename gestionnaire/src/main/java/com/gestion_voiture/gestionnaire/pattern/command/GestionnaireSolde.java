package com.gestion_voiture.gestionnaire.pattern.command;

import java.util.Stack;

import org.springframework.stereotype.Component;

@Component
public class GestionnaireSolde {
    // Pile pour stocker les commandes exécutées (LIFO pour l'annulation)
    private final Stack<Command> historique = new Stack<>();

    public void executerCommande(Command commande) {
        commande.execute();
        historique.push(commande);
    }

    public void annulerDerniereSolde() {
        if (!historique.isEmpty()) {
            Command commande = historique.pop();
            commande.undo();
        }
    }
}