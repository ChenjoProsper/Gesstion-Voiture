package com.gestion_voiture.gestionnaire.patterns.builder;

import com.gestion_voiture.gestionnaire.models.*;


public class LiasseBuilderHTML implements LiasseBuilder {
    private LiasseDocuments liasse = new LiasseDocuments();
     @Override
    public void construireBonCommande() {
        liasse.ajouteDocument(new BonCommande());
    }

    @Override
    public void construireCertificat() {
        liasse.ajouteDocument(new CertificatCession());
    }

    @Override
    public void construireDemandeImmatriculation() {
        liasse.ajouteDocument(new DemandeImmatriculation());
    }

    @Override
    public LiasseDocuments getResultat() {
        return liasse;
    }
}