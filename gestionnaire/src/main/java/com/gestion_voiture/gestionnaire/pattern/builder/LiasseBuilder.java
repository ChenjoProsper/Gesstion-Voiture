package com.gestion_voiture.gestionnaire.pattern.builder;

import com.gestion_voiture.gestionnaire.models.LiasseDocuments;

import com.gestion_voiture.gestionnaire.models.Commande;

public interface LiasseBuilder {
    void reset();
    void construireBonCommande(Commande commande);
    void construireCertificat(Commande commande);
    void construireDemandeImmatriculation(Commande commande);
    LiasseDocuments getResultat();
}
