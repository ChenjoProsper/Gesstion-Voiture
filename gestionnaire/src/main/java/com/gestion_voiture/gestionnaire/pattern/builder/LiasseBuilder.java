package com.gestion_voiture.gestionnaire.pattern.builder;

import com.gestion_voiture.gestionnaire.models.LiasseDocuments;

public interface LiasseBuilder {
    void construireBonCommande();
    void construireCertificat();
    void construireDemandeImmatriculation();
    LiasseDocuments getResultat();
}
