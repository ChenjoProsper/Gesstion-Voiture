package com.gestion_voiture.gestionnaire.services.impl;

import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;
import com.gestion_voiture.gestionnaire.mapper.CommandeMapper;
import com.gestion_voiture.gestionnaire.models.Client;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.Option;
import com.gestion_voiture.gestionnaire.models.PanierItem;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.models.Enum.EtatCommande;
import com.gestion_voiture.gestionnaire.pattern.decorator.ComposantVehicule;
import com.gestion_voiture.gestionnaire.pattern.decorator.OptionDecorateur;
import com.gestion_voiture.gestionnaire.repository.ClientRepository;
import com.gestion_voiture.gestionnaire.repository.CommandeRepository;
import com.gestion_voiture.gestionnaire.repository.OptionRepository;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.services.CommandeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ServiceLiasse serviceLiasse;
    private final CommandeMapper commandeMapper;
    private final OptionRepository optionRepository;
    private final com.gestion_voiture.gestionnaire.pattern.factory.CommandeComptantCreator comptantCreator;
    private final com.gestion_voiture.gestionnaire.pattern.factory.CommandeCreditCreator creditCreator;
    private final com.gestion_voiture.gestionnaire.repository.PanierRepository panierRepository;

    @Override
    @Transactional
    public CommandeResultDTO passerCommande(CommandeDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        List<Vehicule> vehicules = vehiculeRepository.findAllById(dto.getVehiculesIds());

        Commande commande = commandeMapper.toEntity(dto);
        commande.setClient(client);
        commande.setVehicules(vehicules);
        commande.setDateCommande(LocalDateTime.now());

        Commande savedCommande = commandeRepository.save(commande);
        
        return commandeMapper.toDto(savedCommande);
    }

    @Override
    @Transactional
    public CommandeResultDTO passerCommandeDepuisPanier(Long clientId, String typePaiement, String paysLivraison) {
        var client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client non trouvé"));

        var panierOpt = panierRepository.findByClientId(clientId);
        if (panierOpt.isEmpty()) throw new RuntimeException("Panier introuvable pour le client");

        var panier = panierOpt.get();

        // Choisir le créateur selon le type de paiement
        Commande commande;
        if ("CREDIT".equalsIgnoreCase(typePaiement)) {
            commande = creditCreator.creerCommande();
        } else {
            commande = comptantCreator.creerCommande();
        }

        commande.setClient(client);
        commande.setDateCommande(java.time.LocalDateTime.now());
        commande.setPaysLivraison(paysLivraison);

        double sommeDecorEE = 0.0;

        // Construire la liste des véhicules et calculer le prix décoré incluant options
        for (PanierItem item : panier.getItems()) {
            Vehicule base = item.getVehicule();
            List<Option> opts = item.getOptions() != null ? item.getOptions() : new java.util.ArrayList<>();

            // Vérifier une dernière fois la compatibilité
            for (int i = 0; i < opts.size(); i++) {
                for (int j = i + 1; j < opts.size(); j++) {
                    if (!opts.get(i).estCompatible(opts.get(j)) || !opts.get(j).estCompatible(opts.get(i))) {
                        throw new RuntimeException("Options incompatibles pour le véhicule " + base.getId());
                    }
                }
            }

            // Décorer pour calcul du prix
            com.gestion_voiture.gestionnaire.pattern.decorator.ComposantVehicule deco = base;
            for (Option o : opts) {
                deco = new com.gestion_voiture.gestionnaire.pattern.decorator.OptionDecorateur(deco, o);
            }

            int q = item.getQuantite() != null ? item.getQuantite() : 1;
            sommeDecorEE += deco.calculePrix() * q;

            // Ajouter le véhicule 'q' fois dans la commande (simplification)
            for (int k = 0; k < q; k++) {
                commande.ajouterVehicule(base);
            }
        }

        // Appliquer frais spécifiques (approximé) : si credit, multiplier par (1 + tauxInteret)
        if (commande instanceof com.gestion_voiture.gestionnaire.models.CommandeCredit) {
            com.gestion_voiture.gestionnaire.models.CommandeCredit cc = (com.gestion_voiture.gestionnaire.models.CommandeCredit) commande;
            Double taux = cc.getTauxInteret() != null ? cc.getTauxInteret() : 0.0;
            sommeDecorEE = sommeDecorEE * (1 + taux);
        }

        // Calculer taxes
        double taxe = "Cameroun".equalsIgnoreCase(paysLivraison) ? 0.192 : 0.20;
        double totalFinal = sommeDecorEE + (sommeDecorEE * taxe);

        commande.setMontantTotal(totalFinal);

        Commande saved = commandeRepository.save(commande);

        // Vider le panier après commande
        panier.getItems().clear();
        panierRepository.save(panier);

        return commandeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public Double calculerPrixFinal(Long commandeId, Map<Long, List<Long>> optionsParVehicule) {

        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        double total = 0.0;

        for (Vehicule vehicule : commande.getVehicules()) {
            ComposantVehicule vehiculeCalcule = vehicule;

            List<Long> optionsIds = optionsParVehicule.get(vehicule.getId());
            if (optionsIds != null) {
                List<Option> options = optionRepository.findAllById(optionsIds);
                for (Option opt : options) {
                    vehiculeCalcule = new OptionDecorateur(vehiculeCalcule, opt);
                }
            }

            total += vehiculeCalcule.calculePrix();
        }

        return total;
    }


    @Override
    @Transactional
    public void validerCommande(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        commande.ajouterObservateur(serviceLiasse);

        commande.setEtat(EtatCommande.VALIDE);

        commandeRepository.save(commande);
    }
}