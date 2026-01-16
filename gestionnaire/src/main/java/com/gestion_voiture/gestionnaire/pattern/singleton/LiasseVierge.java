package com.gestion_voiture.gestionnaire.pattern.singleton;

import com.gestion_voiture.gestionnaire.models.DocumentAdministratif;
import com.gestion_voiture.gestionnaire.models.LiasseDocuments;

public class LiasseVierge {
    private static LiasseDocuments instance = null;

    private LiasseVierge() {
    }

    public static synchronized LiasseDocuments getInstance() {
        if (instance == null) {
            instance = new LiasseDocuments();

            // On peuple avec des documents concrets (Administratifs)
            instance.ajouteDocument(new DocumentAdministratif("Certificat de Cession", "Modèle vierge de cession..."));
            instance.ajouteDocument(
                    new DocumentAdministratif("Bon de Commande", "Modèle vierge de bon de commande..."));
            instance.ajouteDocument(
                    new DocumentAdministratif("Demande d'Immatriculation", "Modèle vierge de demande..."));
        }
        return instance;
    }
}