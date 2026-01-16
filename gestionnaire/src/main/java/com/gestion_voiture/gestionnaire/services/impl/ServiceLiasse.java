package com.gestion_voiture.gestionnaire.services.impl;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.LiasseDocuments;
import com.gestion_voiture.gestionnaire.models.Societe;
import com.gestion_voiture.gestionnaire.models.Enum.EtatCommande;
import com.gestion_voiture.gestionnaire.pattern.builder.LiasseBuilderHTML;
import com.gestion_voiture.gestionnaire.pattern.builder.LiasseBuilderPDF;
import com.gestion_voiture.gestionnaire.pattern.observer.CommandeSujet;
import com.gestion_voiture.gestionnaire.pattern.observer.Observateur;
import com.gestion_voiture.gestionnaire.pattern.singleton.LiasseVierge;
import com.gestion_voiture.gestionnaire.repository.CommandeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceLiasse implements Observateur {

    private final LiasseBuilderPDF pdfBuilder;
    private final LiasseBuilderHTML htmlBuilder;
    private final CommandeRepository commandeRepository;

    @Override
    public void actualiser(CommandeSujet sujet) {
        if (EtatCommande.VALIDE.name().equals(sujet.getEtatNom()) && sujet instanceof Commande commande) {

            // Récupérer la liasse vierge via le singleton
            LiasseDocuments liasseVierge = LiasseVierge.getInstance();

            // Utiliser la liasse vierge comme base pour construire les documents
            var builder = (commande.getClient() instanceof Societe) ? htmlBuilder : pdfBuilder;

            builder.reset();
            builder.construireBonCommande(commande);
            builder.construireCertificat(commande);
            builder.construireDemandeImmatriculation(commande);

            // Récupérer la liasse construite et l'assigner à la commande
            LiasseDocuments liasseComplete = builder.getResultat();
            commande.setLiasseDocuments(liasseComplete);

            commandeRepository.save(commande);
        }
    }
}