package com.gestion_voiture.gestionnaire.pattern.builder;

import org.springframework.stereotype.Component;
import com.gestion_voiture.gestionnaire.models.*;
import java.time.LocalDateTime;

@Component
public class LiasseBuilderHTML implements LiasseBuilder {
    private LiasseDocuments liasse;

    @Override
    public void reset() {
        this.liasse = new LiasseDocuments();
    }

    @Override
    public void construireBonCommande(Commande commande) {
        BonCommande bon = new BonCommande();
        bon.setTitre("Bon de Commande HTML #" + commande.getId());
        bon.setContenu("<html><body><h1>Commande de " + commande.getClient().getNom() + "</h1>" +
                        "<p>Total à payer : <b>" + commande.calculeMontantTotal() + " €</b></p></body></html>");
        bon.setDateCreation(LocalDateTime.now());
        liasse.ajouteDocument(bon);
    }

    @Override
    public void construireCertificat(Commande commande) {
        CertificatCession cert = new CertificatCession();
        cert.setTitre("Certificat HTML");
        cert.setContenu("<html><body><p>Véhicule cédé le : " + LocalDateTime.now() + "</p></body></html>");
        liasse.ajouteDocument(cert);
    }

    @Override
    public void construireDemandeImmatriculation(Commande commande) {
        DemandeImmatriculation immat = new DemandeImmatriculation();
        immat.setTitre("Immat HTML");
        immat.setContenu("<html><body><p>Demande pour la région de : " + commande.getPaysLivraison() + "</p></body></html>");
        liasse.ajouteDocument(immat);
    }

    @Override
    public LiasseDocuments getResultat() {
        return liasse;
    }
}