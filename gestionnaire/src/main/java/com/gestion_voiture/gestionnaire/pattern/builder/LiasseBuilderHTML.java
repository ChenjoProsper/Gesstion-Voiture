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
            
            StringBuilder contenuHtml = new StringBuilder();
            contenuHtml.append("<html><body style='font-family: Arial, sans-serif;'>")
                    .append("<div style='text-align: center; border-bottom: 2px solid #000; padding-bottom: 10px;'>");
            contenuHtml.append("<h1>BON DE COMMANDE</h1>");
            contenuHtml.append("<p style='color: #666;'>Document officiel de commande de véhicule</p>");
            contenuHtml.append("</div>");
            
            contenuHtml.append("<h2>INFORMATIONS GÉNÉRALES</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr><td style='padding: 5px;'><b>Numéro de Commande:</b></td><td style='padding: 5px;'>").append(commande.getId()).append("</td></tr>")
                    .append("<tr><td style='padding: 5px;'><b>Date de Commande:</b></td><td style='padding: 5px;'>").append(LocalDateTime.now()).append("</td></tr>")
                    .append("<tr><td style='padding: 5px;'><b>État:</b></td><td style='padding: 5px; color: blue;'>").append(commande.getEtat().name()).append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>INFORMATIONS CLIENT</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr><td style='padding: 5px;'><b>Nom/Raison Sociale:</b></td><td style='padding: 5px;'>").append(commande.getClient().getNom()).append("</td></tr>")
                    .append("<tr><td style='padding: 5px;'><b>Email:</b></td><td style='padding: 5px;'>").append(commande.getClient().getEmail()).append("</td></tr>")
                    .append("<tr><td style='padding: 5px;'><b>Téléphone:</b></td><td style='padding: 5px;'>").append(commande.getClient().getTelephone() != null ? commande.getClient().getTelephone() : "N/A").append("</td></tr>")
                    .append("<tr><td style='padding: 5px;'><b>Pays de Livraison:</b></td><td style='padding: 5px;'>").append(commande.getPaysLivraison()).append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>DÉTAILS DU VÉHICULE COMMANDÉ</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse; border: 1px solid #000;'>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Marque</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getMarque()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Modèle</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getModele()).append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Référence</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getReference() != null ? vehicule.getReference() : "N/A").append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Description</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getDescription() != null ? vehicule.getDescription() : "N/A").append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Prix de Base</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(String.format("%.2f", vehicule.getPrixBase())).append(" €</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Remise Appliquée</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getPourcentageSolde() != null ? vehicule.getPourcentageSolde() + "%" : "Aucune").append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>RÉCAPITULATIF FINANCIER</h2>")
                    .append("<table style='width: 60%; margin-left: 20%; border-collapse: collapse; border: 2px solid #000;'>")
                    .append("<tr style='background-color: #ffffcc;'><td style='padding: 10px; border: 1px solid #000; font-size: 16px;'><b>MONTANT TOTAL À PAYER</b></td><td style='padding: 10px; border: 1px solid #000; color: red; font-size: 18px; font-weight: bold; text-align: right;'>").append(String.format("%.2f", commande.getMontantTotal())).append(" €</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>TYPE DE PAIEMENT</h2>")
                    .append("<p style='font-size: 14px; padding: 10px; background-color: #e8f4f8; border-left: 4px solid #0066cc;'>").append("Type: <b>COMPTANT</b></p>");
            
            contenuHtml.append("<div style='margin-top: 40px; border-top: 1px solid #000; padding-top: 20px;'>");
            contenuHtml.append("<p style='text-align: right;'>Signature du client: _________________</p>");
            contenuHtml.append("<p style='text-align: right;'>Signature du vendeur: _________________</p>");
            contenuHtml.append("</div>");
            
            contenuHtml.append("<p style='text-align: center; font-size: 12px; color: #666; margin-top: 30px;'>").append("Ce document est valide à titre informatif et de confirmation de commande.").append("</p>");
            contenuHtml.append("</body></html>");
            
            bon.setContenu(contenuHtml.toString());
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
            
            StringBuilder contenuHtml = new StringBuilder();
            contenuHtml.append("<html><body style='font-family: Arial, sans-serif;'>")
                    .append("<div style='text-align: center; border: 3px double #000; padding: 20px; margin-bottom: 20px;'>");
            contenuHtml.append("<h1 style='margin: 0;'>CERTIFICAT DE CESSION DE VÉHICULE</h1>");
            contenuHtml.append("<p style='color: #666; margin: 5px 0;'>Document officiel de transfert de propriété</p>");
            contenuHtml.append("</div>");
            
            contenuHtml.append("<h2>INFORMATION GÉNÉRALE</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr><td style='padding: 5px;'><b>Numéro de Commande:</b></td><td style='padding: 5px;'>").append(commande.getId()).append("</td></tr>")
                    .append("<tr><td style='padding: 5px;'><b>Date de Cession:</b></td><td style='padding: 5px;'>").append(LocalDateTime.now()).append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>PARTIES IMPLIQUÉES</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse; border: 1px solid #000;'>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>CÉDANT (Vendeur)</b></td><td style='padding: 8px; border: 1px solid #000;'>Gestion Voiture SARL</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>CESSIONNAIRE (Acheteur)</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getClient().getNom()).append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Email du Cessionnaire</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getClient().getEmail()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Téléphone</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getClient().getTelephone() != null ? commande.getClient().getTelephone() : "N/A").append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>CARACTÉRISTIQUES DÉTAILLÉES DU VÉHICULE CÉDÉ</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse; border: 1px solid #000;'>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000; width: 40%;'><b>Marque</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getMarque()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Modèle</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getModele()).append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Référence Commerciale</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getReference() != null ? vehicule.getReference() : "N/A").append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Description</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getDescription() != null ? vehicule.getDescription() : "N/A").append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>CONDITIONS DE CESSION</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse; border: 1px solid #000;'>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Prix de Cession</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(String.format("%.2f", commande.getMontantTotal())).append(" €</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Pays de Livraison</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getPaysLivraison()).append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Modalités de Paiement</b></td><td style='padding: 8px; border: 1px solid #000;'>COMPTANT</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>État du Véhicule</b></td><td style='padding: 8px; border: 1px solid #000;'>Neuf en stock</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>ENGAGEMENTS ET RESPONSABILITÉS</h2>")
                    .append("<ul>")
                    .append("<li>Le cédant garantit être propriétaire du véhicule et avoir le droit de le céder.</li>")
                    .append("<li>Le véhicule est vendu dans l'état où il se trouve, sans garantie cachée.</li>")
                    .append("<li>Le cessionnaire accepte l'achat du véhicule aux conditions mentionnées ci-dessus.</li>")
                    .append("<li>La responsabilité du cédant cesse à la date de signature du présent certificat.</li>")
                    .append("</ul>");
            
            contenuHtml.append("<div style='margin-top: 40px; border-top: 2px solid #000; padding-top: 20px;'>")
                    .append("<table style='width: 100%;'>")
                    .append("<tr>")
                    .append("<td style='text-align: center; width: 50%;'><p><b>CÉDANT</b></p><p>Signature: _________________</p><p>Date: _________________</p></td>")
                    .append("<td style='text-align: center; width: 50%;'><p><b>CESSIONNAIRE</b></p><p>Signature: _________________</p><p>Date: _________________</p></td>")
                    .append("</tr>")
                    .append("</table>")
                    .append("</div>");
            
            contenuHtml.append("<p style='text-align: center; font-size: 11px; color: #999; margin-top: 30px; border-top: 1px solid #ccc; padding-top: 10px;'>").append("Certificat établi en double exemplaire - Un exemplaire pour chaque partie - Non transférable").append("</p>");
            contenuHtml.append("</body></html>");
            
            cert.setContenu(contenuHtml.toString());
            cert.setDateCreation(LocalDateTime.now());
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
            
            StringBuilder contenuHtml = new StringBuilder();
            contenuHtml.append("<html><body style='font-family: Arial, sans-serif;'>")
                    .append("<div style='text-align: center; border-bottom: 3px solid #0066cc; padding-bottom: 15px; margin-bottom: 20px;'>");
            contenuHtml.append("<h1 style='color: #0066cc; margin: 0;'>DEMANDE D'IMMATRICULATION DE VÉHICULE</h1>");
            contenuHtml.append("<p style='color: #666; margin: 5px 0;'>Formulaire de demande auprès de l'autorité compétente</p>");
            contenuHtml.append("</div>");
            
            contenuHtml.append("<h2>RÉFÉRENCE DE DOSSIER</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr><td style='padding: 5px;'><b>Numéro de Commande:</b></td><td style='padding: 5px;'>").append(commande.getId()).append("</td></tr>")
                    .append("<tr><td style='padding: 5px;'><b>Date de Demande:</b></td><td style='padding: 5px;'>").append(LocalDateTime.now()).append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>INFORMATIONS DU PROPRIÉTAIRE</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse; border: 1px solid #000;'>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Nom/Raison Sociale</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getClient().getNom()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Email</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getClient().getEmail()).append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Téléphone</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getClient().getTelephone() != null ? commande.getClient().getTelephone() : "N/A").append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Pays de Résidence</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(commande.getPaysLivraison()).append("</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>CARACTÉRISTIQUES DÉTAILLÉES DU VÉHICULE</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse; border: 1px solid #000;'>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000; width: 40%;'><b>Marque du Constructeur</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getMarque()).append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Modèle</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getModele()).append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Référence Commerciale</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getReference() != null ? vehicule.getReference() : "N/A").append("</td></tr>")
                    .append("<tr><td style='padding: 8px; border: 1px solid #000;'><b>Description Technique</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(vehicule.getDescription() != null ? vehicule.getDescription() : "N/A").append("</td></tr>")
                    .append("<tr style='background-color: #f0f0f0;'><td style='padding: 8px; border: 1px solid #000;'><b>Prix d'Acquisition</b></td><td style='padding: 8px; border: 1px solid #000;'>").append(String.format("%.2f", commande.getMontantTotal())).append(" €</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>DOCUMENTS FOURNIS - VÉRIFICATION</h2>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tr><td style='padding: 8px;'>☑ Bon de Commande - Reçu et vérifié</td></tr>")
                    .append("<tr><td style='padding: 8px;'>☑ Certificat de Cession - Reçu et vérifié</td></tr>")
                    .append("<tr><td style='padding: 8px;'>☑ Documents d'identification du propriétaire - En cours</td></tr>")
                    .append("<tr><td style='padding: 8px;'>☑ Preuve de domicile - À fournir avant immatriculation</td></tr>")
                    .append("<tr><td style='padding: 8px;'>☑ Attestation de non-gage - À demander auprès du cédant</td></tr>")
                    .append("</table>");
            
            contenuHtml.append("<h2>STATUT ET DÉLAIS D'IMMATRICULATION</h2>")
                    .append("<div style='background-color: #ffffcc; padding: 15px; border-left: 4px solid #ff9900; margin-bottom: 20px;'>")
                    .append("<p><b>Statut Actuel:</b> En cours de traitement</p>")
                    .append("<p><b>Délai Estimé:</b> 7 à 15 jours ouvrables</p>")
                    .append("<p><b>Numéro d'Immatriculation Temporaire:</b> À assigner lors du traitement</p>")
                    .append("<p><b>Date d'Immatriculation Prévue:</b> _______________</p>")
                    .append("</div>");
            
            contenuHtml.append("<h2>NOTES IMPORTANTES</h2>")
                    .append("<ul>")
                    .append("<li>Cette demande d'immatriculation doit être soumise aux autorités compétentes du pays de résidence.</li>")
                    .append("<li>Tous les documents mentionnés doivent être fournis avant le traitement final.</li>")
                    .append("<li>Le propriétaire est responsable de la fourniture de tous les documents requis.</li>")
                    .append("<li>Les frais d'immatriculation sont à la charge du propriétaire selon la législation locale.</li>")
                    .append("</ul>");
            
            contenuHtml.append("<div style='margin-top: 40px; border-top: 2px solid #000; padding-top: 20px;'>")
                    .append("<p><b>Demande initiée par:</b> Gestion Voiture SARL</p>")
                    .append("<p><b>Signature et tampon:</b> _________________</p>")
                    .append("<p><b>Date:</b> _________________</p>")
                    .append("</div>");
            
            contenuHtml.append("<p style='text-align: center; font-size: 11px; color: #999; margin-top: 30px;'>").append("Document généré automatiquement - Signature requise avant transmission aux autorités").append("</p>");
            contenuHtml.append("</body></html>");
            
            immat.setContenu(contenuHtml.toString());
            immat.setDateCreation(LocalDateTime.now());
            liasse.ajouteDocument(immat);
        }
    }

    @Override
    public LiasseDocuments getResultat() {
        return liasse;
    }
}