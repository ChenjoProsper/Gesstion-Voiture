package com.gestion_voiture.gestionnaire.pattern.observer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.DemandeImmatriculation;
import com.gestion_voiture.gestionnaire.models.Enum.EtatCommande;
import com.gestion_voiture.gestionnaire.repository.DocumentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServiceImmatriculation implements Observateur {
    
    private final DocumentRepository documentRepository;

    @Override
    public void actualiser(CommandeSujet sujet) {
        if (EtatCommande.VALIDE.name().equals(sujet.getEtatNom())) {
            Commande cmd = (Commande) sujet;
            
            DemandeImmatriculation doc = new DemandeImmatriculation();
            doc.setNom("Certificat d'immatriculation pour Commande #" + cmd.getId());
            doc.setDateCreation(LocalDateTime.now());
            
            documentRepository.save(doc);
            System.out.println("DOC GÉNÉRÉ : Le document d'immatriculation a été créé en base.");
        }
    }

}
