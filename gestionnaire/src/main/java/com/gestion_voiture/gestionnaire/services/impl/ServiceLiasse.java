package com.gestion_voiture.gestionnaire.services.impl;

import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.LiasseDocuments;
import com.gestion_voiture.gestionnaire.models.Societe;
import com.gestion_voiture.gestionnaire.models.Enum.EtatCommande;
import com.gestion_voiture.gestionnaire.pattern.builder.LiasseBuilderHTML;
import com.gestion_voiture.gestionnaire.pattern.builder.LiasseBuilderPDF;
import com.gestion_voiture.gestionnaire.pattern.observer.CommandeSujet;
import com.gestion_voiture.gestionnaire.pattern.observer.Observateur;
import com.gestion_voiture.gestionnaire.repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceLiasse implements Observateur {

    private final LiasseBuilderPDF pdfBuilder;
    private final LiasseBuilderHTML htmlBuilder;
    private final CommandeRepository commandeRepository;

    @Override
    public void actualiser(CommandeSujet sujet) {
        if (EtatCommande.VALIDE.name().equals(sujet.getEtatNom()) && sujet instanceof Commande commande) {
    
            var builder = (commande.getClient() instanceof Societe) ? htmlBuilder : pdfBuilder;

            builder.reset();
            builder.construireBonCommande(commande);
            builder.construireCertificat(commande);
            builder.construireDemandeImmatriculation(commande);

            LiasseDocuments liasse = builder.getResultat();
            commande.setLiasseDocuments(liasse);

            commandeRepository.save(commande);
        }
    }
}