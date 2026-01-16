package com.gestion_voiture.gestionnaire.pattern.builder;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.models.BonCommande;
import com.gestion_voiture.gestionnaire.models.CertificatCession;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.DemandeImmatriculation;
import com.gestion_voiture.gestionnaire.models.LiasseDocuments;
import com.gestion_voiture.gestionnaire.models.Vehicule;

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
        Vehicule vehicule = commande.getVehicules() != null && !commande.getVehicules().isEmpty()
                ? commande.getVehicules().get(0)
                : null;

        if (vehicule != null) {
            bon.setTitre(
                    "Bon de Commande PDF #" + commande.getId() + " - " + vehicule.getMarque() + " "
                            + vehicule.getModele());
            bon.setContenu(
                    "=== BON DE COMMANDE ===\n" +
                            "Numéro de Commande: " + commande.getId() + "\n" +
                            "Date: " + LocalDateTime.now() + "\n" +
                            "Client: " + commande.getClient().getNom() + "\n\n" +
                            "=== DÉTAILS DU VÉHICULE ===\n" +
                            "Marque: " + vehicule.getMarque() + "\n" +
                            "Modèle: " + vehicule.getModele() + "\n" +
                            "Prix de base: " + vehicule.getPrixBase() + "€\n" +
                            "Remise appliquée: "
                            + (vehicule.getPourcentageSolde() != null ? vehicule.getPourcentageSolde() + "%" : "Aucune")
                            + "\n\n" +
                            "=== MONTANT ===\n" +
                            "Montant total: " + commande.getMontantTotal() + "€\n" +
                            "État: " + commande.getEtat().name());
            bon.setDateCreation(LocalDateTime.now());
            liasse.ajouteDocument(bon);
        }
    }

    @Override
    public void construireCertificat(Commande commande) {
        CertificatCession cert = new CertificatCession();
        Vehicule vehicule = commande.getVehicules() != null && !commande.getVehicules().isEmpty()
                ? commande.getVehicules().get(0)
                : null;

        if (vehicule != null) {
            cert.setTitre("Certificat de Cession PDF - " + vehicule.getMarque() + " " + vehicule.getModele());
            cert.setContenu(
                    "=== CERTIFICAT DE CESSION ===\n\n" +
                            "Cédant: [Concessionnaire]\n" +
                            "Cessionnaire: " + commande.getClient().getNom() + "\n\n" +
                            "=== VÉHICULE ===\n" +
                            "Marque: " + vehicule.getMarque() + "\n" +
                            "Modèle: " + vehicule.getModele() + "\n" +
                            "Référence: " + (vehicule.getReference() != null ? vehicule.getReference() : "N/A") + "\n\n"
                            +
                            "=== CONDITIONS DE CESSION ===\n" +
                            "Date de cession: " + LocalDateTime.now() + "\n" +
                            "Prix de cession: " + commande.getMontantTotal() + "€\n" +
                            "Pays de livraison: " + commande.getPaysLivraison() + "\n\n" +
                            "Signature du cédant: ___________\n" +
                            "Signature du cessionnaire: ___________");
            liasse.ajouteDocument(cert);
        }
    }

    @Override
    public void construireDemandeImmatriculation(Commande commande) {
        DemandeImmatriculation immat = new DemandeImmatriculation();
        Vehicule vehicule = commande.getVehicules() != null && !commande.getVehicules().isEmpty()
                ? commande.getVehicules().get(0)
                : null;

        if (vehicule != null) {
            immat.setTitre("Demande d'Immatriculation PDF - " + vehicule.getMarque() + " " + vehicule.getModele());
            immat.setContenu(
                    "=== DEMANDE D'IMMATRICULATION ===\n\n" +
                            "Propriétaire: " + commande.getClient().getNom() + "\n" +
                            "Pays: " + commande.getPaysLivraison() + "\n" +
                            "Date de demande: " + LocalDateTime.now() + "\n\n" +
                            "=== CARACTÉRISTIQUES DU VÉHICULE ===\n" +
                            "Marque: " + vehicule.getMarque() + "\n" +
                            "Modèle: " + vehicule.getModele() + "\n" +
                            "Référence: " + (vehicule.getReference() != null ? vehicule.getReference() : "N/A") + "\n" +
                            "Description: " + (vehicule.getDescription() != null ? vehicule.getDescription() : "N/A")
                            + "\n\n" +
                            "=== VALIDATIONS ===\n" +
                            "Certificat de cession: ☐ Reçu\n" +
                            "Bon de commande: ☐ Reçu\n" +
                            "Documents propriétaire: ☐ Reçu\n\n" +
                            "Date d'immatriculation prévue: ___________");
            liasse.ajouteDocument(immat);
        }
    }

    @Override
    public LiasseDocuments getResultat() {
        return liasse;
    }
}