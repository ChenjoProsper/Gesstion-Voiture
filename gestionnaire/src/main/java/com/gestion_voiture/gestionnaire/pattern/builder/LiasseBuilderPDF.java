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
            
            StringBuilder contenuPdf = new StringBuilder();
            contenuPdf.append("═════════════════════════════════════════════════════════════════════\n")
                    .append("                      BON DE COMMANDE\n")
                    .append("              Document officiel de commande de véhicule\n")
                    .append("═════════════════════════════════════════════════════════════════════\n\n");
            
            contenuPdf.append("INFORMATIONS GÉNÉRALES\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Numéro de Commande:     ").append(commande.getId()).append("\n")
                    .append("Date de Commande:       ").append(LocalDateTime.now()).append("\n")
                    .append("État:                   ").append(commande.getEtat().name()).append("\n\n");
            
            contenuPdf.append("INFORMATIONS CLIENT\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Nom/Raison Sociale:     ").append(commande.getClient().getNom()).append("\n")
                    .append("Email:                  ").append(commande.getClient().getEmail()).append("\n")
                    .append("Téléphone:              ").append(commande.getClient().getTelephone() != null ? commande.getClient().getTelephone() : "N/A").append("\n")
                    .append("Pays de Livraison:      ").append(commande.getPaysLivraison()).append("\n\n");
            
            contenuPdf.append("DÉTAILS DU VÉHICULE COMMANDÉ\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Marque:                 ").append(vehicule.getMarque()).append("\n")
                    .append("Modèle:                 ").append(vehicule.getModele()).append("\n")
                    .append("Référence:              ").append(vehicule.getReference() != null ? vehicule.getReference() : "N/A").append("\n")
                    .append("Description:            ").append(vehicule.getDescription() != null ? vehicule.getDescription() : "N/A").append("\n")
                    .append("Prix de Base:           ").append(String.format("%.2f", vehicule.getPrixBase())).append(" €\n")
                    .append("Remise Appliquée:       ").append(vehicule.getPourcentageSolde() != null ? vehicule.getPourcentageSolde() + "%" : "Aucune").append("\n\n");
            
            contenuPdf.append("═════════════════════════════════════════════════════════════════════\n")
                    .append("RÉCAPITULATIF FINANCIER\n")
                    .append("═════════════════════════════════════════════════════════════════════\n")
                    .append("MONTANT TOTAL À PAYER:  ").append(String.format("%.2f", commande.getMontantTotal())).append(" €\n")
                    .append("═════════════════════════════════════════════════════════════════════\n\n");
            
            contenuPdf.append("TYPE DE PAIEMENT\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Type: COMPTANT\n\n");
            
            contenuPdf.append("\n\nSignatures:\n\n")
                    .append("Signature du client: _____________________\n")
                    .append("Signature du vendeur: _____________________\n\n")
                    .append("═════════════════════════════════════════════════════════════════════\n")
                    .append("Ce document est valide à titre informatif et de confirmation de commande.\n");
            
            bon.setContenu(contenuPdf.toString());
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
            
            StringBuilder contenuPdf = new StringBuilder();
            contenuPdf.append("═════════════════════════════════════════════════════════════════════\n")
                    .append("                CERTIFICAT DE CESSION DE VÉHICULE\n")
                    .append("             Document officiel de transfert de propriété\n")
                    .append("═════════════════════════════════════════════════════════════════════\n\n");
            
            contenuPdf.append("INFORMATION GÉNÉRALE\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Numéro de Commande:     ").append(commande.getId()).append("\n")
                    .append("Date de Cession:        ").append(LocalDateTime.now()).append("\n\n");
            
            contenuPdf.append("PARTIES IMPLIQUÉES\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("CÉDANT (Vendeur):       Gestion Voiture SARL\n")
                    .append("CESSIONNAIRE (Acheteur): ").append(commande.getClient().getNom()).append("\n")
                    .append("Email du Cessionnaire:  ").append(commande.getClient().getEmail()).append("\n")
                    .append("Téléphone:              ").append(commande.getClient().getTelephone() != null ? commande.getClient().getTelephone() : "N/A").append("\n\n");
            
            contenuPdf.append("CARACTÉRISTIQUES DÉTAILLÉES DU VÉHICULE CÉDÉ\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Marque:                 ").append(vehicule.getMarque()).append("\n")
                    .append("Modèle:                 ").append(vehicule.getModele()).append("\n")
                    .append("Référence Commerciale:  ").append(vehicule.getReference() != null ? vehicule.getReference() : "N/A").append("\n")
                    .append("Description:            ").append(vehicule.getDescription() != null ? vehicule.getDescription() : "N/A").append("\n\n");
            
            contenuPdf.append("CONDITIONS DE CESSION\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Prix de Cession:        ").append(String.format("%.2f", commande.getMontantTotal())).append(" €\n")
                    .append("Pays de Livraison:      ").append(commande.getPaysLivraison()).append("\n")
                    .append("Modalités de Paiement:  COMPTANT\n")
                    .append("État du Véhicule:       Neuf en stock\n\n");
            
            contenuPdf.append("ENGAGEMENTS ET RESPONSABILITÉS\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("• Le cédant garantit être propriétaire du véhicule et avoir le droit de le céder.\n")
                    .append("• Le véhicule est vendu dans l'état où il se trouve, sans garantie cachée.\n")
                    .append("• Le cessionnaire accepte l'achat du véhicule aux conditions mentionnées.\n")
                    .append("• La responsabilité du cédant cesse à la date de signature du présent certificat.\n\n");
            
            contenuPdf.append("═════════════════════════════════════════════════════════════════════\n")
                    .append("SIGNATURES\n")
                    .append("═════════════════════════════════════════════════════════════════════\n\n")
                    .append("CÉDANT (Vendeur)\n")
                    .append("Signature: ____________________          Date: ____________________\n\n")
                    .append("CESSIONNAIRE (Acheteur)\n")
                    .append("Signature: ____________________          Date: ____________________\n\n")
                    .append("═════════════════════════════════════════════════════════════════════\n")
                    .append("Certificat établi en double exemplaire - Un exemplaire pour chaque partie\n");
            
            cert.setContenu(contenuPdf.toString());
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
            
            StringBuilder contenuPdf = new StringBuilder();
            contenuPdf.append("═════════════════════════════════════════════════════════════════════\n")
                    .append("              DEMANDE D'IMMATRICULATION DE VÉHICULE\n")
                    .append("         Formulaire de demande auprès de l'autorité compétente\n")
                    .append("═════════════════════════════════════════════════════════════════════\n\n");
            
            contenuPdf.append("RÉFÉRENCE DE DOSSIER\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Numéro de Commande:     ").append(commande.getId()).append("\n")
                    .append("Date de Demande:        ").append(LocalDateTime.now()).append("\n\n");
            
            contenuPdf.append("INFORMATIONS DU PROPRIÉTAIRE\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Nom/Raison Sociale:     ").append(commande.getClient().getNom()).append("\n")
                    .append("Email:                  ").append(commande.getClient().getEmail()).append("\n")
                    .append("Téléphone:              ").append(commande.getClient().getTelephone() != null ? commande.getClient().getTelephone() : "N/A").append("\n")
                    .append("Pays de Résidence:      ").append(commande.getPaysLivraison()).append("\n\n");
            
            contenuPdf.append("CARACTÉRISTIQUES DÉTAILLÉES DU VÉHICULE\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Marque du Constructeur: ").append(vehicule.getMarque()).append("\n")
                    .append("Modèle:                 ").append(vehicule.getModele()).append("\n")
                    .append("Référence Commerciale:  ").append(vehicule.getReference() != null ? vehicule.getReference() : "N/A").append("\n")
                    .append("Description Technique:  ").append(vehicule.getDescription() != null ? vehicule.getDescription() : "N/A").append("\n")
                    .append("Prix d'Acquisition:     ").append(String.format("%.2f", commande.getMontantTotal())).append(" €\n\n");
            
            contenuPdf.append("DOCUMENTS FOURNIS - VÉRIFICATION\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("☑ Bon de Commande - Reçu et vérifié\n")
                    .append("☑ Certificat de Cession - Reçu et vérifié\n")
                    .append("☑ Documents d'identification du propriétaire - En cours\n")
                    .append("☑ Preuve de domicile - À fournir avant immatriculation\n")
                    .append("☑ Attestation de non-gage - À demander auprès du cédant\n\n");
            
            contenuPdf.append("STATUT ET DÉLAIS D'IMMATRICULATION\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("Statut Actuel:              En cours de traitement\n")
                    .append("Délai Estimé:               7 à 15 jours ouvrables\n")
                    .append("Numéro d'Immatriculation:   À assigner lors du traitement\n")
                    .append("Date d'Immatriculation:     _______________\n\n");
            
            contenuPdf.append("NOTES IMPORTANTES\n")
                    .append("─────────────────────────────────────────────────────────────────────\n")
                    .append("• Cette demande d'immatriculation doit être soumise aux autorités\n")
                    .append("  compétentes du pays de résidence.\n")
                    .append("• Tous les documents mentionnés doivent être fournis avant le\n")
                    .append("  traitement final.\n")
                    .append("• Le propriétaire est responsable de la fourniture de tous les\n")
                    .append("  documents requis.\n")
                    .append("• Les frais d'immatriculation sont à la charge du propriétaire selon\n")
                    .append("  la législation locale.\n\n");
            
            contenuPdf.append("═════════════════════════════════════════════════════════════════════\n")
                    .append("SIGNATURES ET VALIDATIONS\n")
                    .append("═════════════════════════════════════════════════════════════════════\n\n")
                    .append("Demande initiée par:    Gestion Voiture SARL\n")
                    .append("Signature et tampon:    ____________________\n")
                    .append("Date:                   ____________________\n\n")
                    .append("═════════════════════════════════════════════════════════════════════\n")
                    .append("Document généré automatiquement\n")
                    .append("Signature requise avant transmission aux autorités\n");
            
            immat.setContenu(contenuPdf.toString());
            immat.setDateCreation(LocalDateTime.now());
            liasse.ajouteDocument(immat);
        }
    }

    @Override
    public LiasseDocuments getResultat() {
        return liasse;
    }
}