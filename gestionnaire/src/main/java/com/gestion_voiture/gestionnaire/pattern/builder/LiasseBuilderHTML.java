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
public class LiasseBuilderHTML implements LiasseBuilder {
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
            bon.setTitre("Bon de Commande HTML #" + commande.getId() + " - " + vehicule.getMarque() + " "
                    + vehicule.getModele());
            bon.setContenu(
                    "<html><body>" +
                            "<h1>BON DE COMMANDE</h1>" +
                            "<p><b>Numéro:</b> " + commande.getId() + "</p>" +
                            "<p><b>Date:</b> " + LocalDateTime.now() + "</p>" +
                            "<p><b>Client:</b> " + commande.getClient().getNom() + "</p>" +
                            "<hr/>" +
                            "<h2>DÉTAILS DU VÉHICULE</h2>" +
                            "<ul>" +
                            "<li><b>Marque:</b> " + vehicule.getMarque() + "</li>" +
                            "<li><b>Modèle:</b> " + vehicule.getModele() + "</li>" +
                            "<li><b>Prix de base:</b> " + vehicule.getPrixBase() + "€</li>" +
                            "<li><b>Remise:</b> "
                            + (vehicule.getPourcentageSolde() != null ? vehicule.getPourcentageSolde() + "%" : "Aucune")
                            + "</li>" +
                            "</ul>" +
                            "<hr/>" +
                            "<h2>MONTANT</h2>" +
                            "<p><b>Montant total à payer: <span style='color:red;font-size:18px;'>"
                            + commande.getMontantTotal() + "€</span></b></p>" +
                            "<p><b>État:</b> " + commande.getEtat().name() + "</p>" +
                            "</body></html>");
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
            cert.setTitre("Certificat de Cession HTML - " + vehicule.getMarque() + " " + vehicule.getModele());
            cert.setContenu(
                    "<html><body>" +
                            "<h1>CERTIFICAT DE CESSION</h1>" +
                            "<hr/>" +
                            "<h2>PARTIES IMPLIQUÉES</h2>" +
                            "<p><b>Cédant:</b> [Concessionnaire]</p>" +
                            "<p><b>Cessionnaire:</b> " + commande.getClient().getNom() + "</p>" +
                            "<hr/>" +
                            "<h2>CARACTÉRISTIQUES DU VÉHICULE</h2>" +
                            "<ul>" +
                            "<li><b>Marque:</b> " + vehicule.getMarque() + "</li>" +
                            "<li><b>Modèle:</b> " + vehicule.getModele() + "</li>" +
                            "<li><b>Référence:</b> "
                            + (vehicule.getReference() != null ? vehicule.getReference() : "N/A")
                            + "</li>" +
                            "</ul>" +
                            "<hr/>" +
                            "<h2>CONDITIONS DE CESSION</h2>" +
                            "<p><b>Date de cession:</b> " + LocalDateTime.now() + "</p>" +
                            "<p><b>Prix de cession:</b> " + commande.getMontantTotal() + "€</p>" +
                            "<p><b>Pays de livraison:</b> " + commande.getPaysLivraison() + "</p>" +
                            "<p><b>Signature du cédant:</b> ___________</p>" +
                            "<p><b>Signature du cessionnaire:</b> ___________</p>" +
                            "</body></html>");
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
            immat.setTitre("Demande d'Immatriculation HTML - " + vehicule.getMarque() + " " + vehicule.getModele());
            immat.setContenu(
                    "<html><body>" +
                            "<h1>DEMANDE D'IMMATRICULATION</h1>" +
                            "<hr/>" +
                            "<h2>INFORMATIONS DU PROPRIÉTAIRE</h2>" +
                            "<p><b>Propriétaire:</b> " + commande.getClient().getNom() + "</p>" +
                            "<p><b>Pays:</b> " + commande.getPaysLivraison() + "</p>" +
                            "<p><b>Date de demande:</b> " + LocalDateTime.now() + "</p>" +
                            "<hr/>" +
                            "<h2>CARACTÉRISTIQUES DU VÉHICULE</h2>" +
                            "<ul>" +
                            "<li><b>Marque:</b> " + vehicule.getMarque() + "</li>" +
                            "<li><b>Modèle:</b> " + vehicule.getModele() + "</li>" +
                            "<li><b>Référence:</b> "
                            + (vehicule.getReference() != null ? vehicule.getReference() : "N/A")
                            + "</li>" +
                            "<li><b>Description:</b> "
                            + (vehicule.getDescription() != null ? vehicule.getDescription() : "N/A") + "</li>" +
                            "</ul>" +
                            "<hr/>" +
                            "<h2>VALIDATIONS</h2>" +
                            "<p>☐ Certificat de cession reçu</p>" +
                            "<p>☐ Bon de commande reçu</p>" +
                            "<p>☐ Documents propriétaire reçus</p>" +
                            "<p><b>Date d'immatriculation prévue:</b> ___________</p>" +
                            "</body></html>");
            liasse.ajouteDocument(immat);
        }
    }

    @Override
    public LiasseDocuments getResultat() {
        return liasse;
    }
}