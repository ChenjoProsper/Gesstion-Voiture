package com.gestion_voiture.gestionnaire.pattern.builder;

import org.springframework.stereotype.Component;
import com.gestion_voiture.gestionnaire.models.*;
import java.time.LocalDateTime;

@Component
public class LiasseBuilderPDF implements LiasseBuilder {

    private LiasseDocuments liasse;

    @Override
    public void reset() {
        this.liasse = new LiasseDocuments();
    }

    @Override
    public void construireBonCommande(Commande commande) {
        BonCommande bon = new BonCommande();
        bon.setTitre("Bon de Commande PDF #" + commande.getId());
        bon.setContenu("Client: " + commande.getClient().getNom() + " | Montant: " + commande.getMontantTotal());
        bon.setDateCreation(LocalDateTime.now());
        liasse.ajouteDocument(bon);
    }

    @Override
    public void construireCertificat(Commande commande) {
        CertificatCession cert = new CertificatCession();
        cert.setTitre("Certificat de Cession PDF");
        cert.setContenu("Cession du véhicule à " + commande.getClient().getNom());
        liasse.ajouteDocument(cert);
    }

    @Override
    public void construireDemandeImmatriculation(Commande commande) {
        DemandeImmatriculation immat = new DemandeImmatriculation();
        immat.setTitre("Demande Immatriculation PDF");
        immat.setContenu("Demande pour pays: " + commande.getPaysLivraison());
        liasse.ajouteDocument(immat);
    }

    @Override
    public LiasseDocuments getResultat() {
        return liasse;
    }
}