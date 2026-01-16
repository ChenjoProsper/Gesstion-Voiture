package com.gestion_voiture.gestionnaire.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.SoldeRequestDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.mapper.VehiculeMapper;
import com.gestion_voiture.gestionnaire.models.Option;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.pattern.abstractfactory.FabriqueVehiculeElectrique;
import com.gestion_voiture.gestionnaire.pattern.abstractfactory.FabriqueVehiculeEssence;
import com.gestion_voiture.gestionnaire.pattern.command.GestionnaireSolde;
import com.gestion_voiture.gestionnaire.pattern.command.SoldeCommand;
import com.gestion_voiture.gestionnaire.pattern.decorator.ComposantVehicule;
import com.gestion_voiture.gestionnaire.pattern.decorator.OptionDecorateur;
import com.gestion_voiture.gestionnaire.repository.OptionRepository;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.services.ImageService;
import com.gestion_voiture.gestionnaire.services.VehiculeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;
    private final OptionRepository optionRepository;
    private final ImageService imageService;
    private final FabriqueVehiculeElectrique fabriqueVehiculeElectrique;
    private final FabriqueVehiculeEssence fabriqueVehiculeEssence;
    private final GestionnaireSolde gestionnaireSolde;

    @Override
    public List<VehiculeResultDTO> listerCatalogue() {

        List<Vehicule> stock = vehiculeRepository.findAll();
        Iterator<Vehicule> it = stock.iterator();

        List<VehiculeResultDTO> results = new ArrayList<>();
        while (it.hasNext()) {
            results.add(vehiculeMapper.toDto(it.next()));
        }
        return results;
    }

    @Override
    public VehiculeResultDTO ajouterVehicule(VehiculeDTO dto) {

        Vehicule v = switch (dto.getTypeVehicule()) {

            case AUTO_ELECTRIQUE -> fabriqueVehiculeElectrique.creerAutomobile();
            case AUTO_ESSENCE -> fabriqueVehiculeEssence.creerAutomobile();
            case SCOOTER_ELECTRIQUE -> fabriqueVehiculeElectrique.creerScooter();
            case SCOOTER_ESSENCE -> fabriqueVehiculeEssence.creerScooter();
        };

        v.setMarque(dto.getMarque());
        v.setModele(dto.getModele());
        v.setPrixBase(dto.getPrixBase());

        // Sauvegarder l'image temporairement avec un ID provisoire
        Vehicule vehiculeSauvegarde = vehiculeRepository.save(v);

        // Sauvegarder l'image avec l'ID réel du véhicule et obtenir le lien
        if (dto.getImageData() != null && !dto.getImageData().isEmpty()) {
            try {
                String imageLink = imageService.sauvegarderImage(dto.getImageData(), vehiculeSauvegarde.getId());
                vehiculeSauvegarde.setImageLink(imageLink);
                vehiculeSauvegarde = vehiculeRepository.save(vehiculeSauvegarde);
            } catch (IOException e) {
                log.error("Erreur lors de la sauvegarde de l'image", e);
                throw new RuntimeException("Erreur lors du stockage de l'image", e);
            }
        }

        return vehiculeMapper.toDto(vehiculeSauvegarde);
    }

    public VehiculeResultDTO ajouterOption(Long vehiculeId, Long optionId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule ID " + vehiculeId + " introuvable"));

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option ID " + optionId + " introuvable"));

        ComposantVehicule vehiculeDecore = new OptionDecorateur(vehicule, option);

        VehiculeResultDTO result = vehiculeMapper.toDto(vehicule);

        result.setPrixBase(vehiculeDecore.calculePrix());
        result.setDescription(vehiculeDecore.getDescription());

        return result;
    }

    @Override
    public Double calculerPrixReel(Long vehiculeId, List<Long> optionsIds) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId).get();

        ComposantVehicule moteurCalcul = vehicule;

        List<Option> options = optionRepository.findAllById(optionsIds);
        for (Option opt : options) {
            moteurCalcul = new OptionDecorateur(moteurCalcul, opt);
        }

        return moteurCalcul.calculePrix();
    }

    // @Override
    // public void solderStock(double pourcentage) {
    // List<Vehicule> tousLesVehicules = vehiculeRepository.findAll();
    // for (Vehicule v : tousLesVehicules) {
    // // On crée et exécute une commande par véhicule
    // SoldeCommand commande = new SoldeCommand(v, pourcentage);
    // gestionnaireSolde.executerCommande(commande);
    // vehiculeRepository.save(v);
    // }
    // }

    @Override
    public void solderStock(SoldeRequestDTO dto) {
        List<Vehicule> vehicules;

        // Si une marque est précisée, on ne solde que celle-là
        if (dto.getMarque() != null && !dto.getMarque().isEmpty()) {
            vehicules = vehiculeRepository.findByMarque(dto.getMarque());
        } else {
            vehicules = vehiculeRepository.findAll();
        }

        for (Vehicule v : vehicules) {
            SoldeCommand commande = new SoldeCommand(v, dto.getPourcentage());
            gestionnaireSolde.executerCommande(commande);
            vehiculeRepository.save(v);
        }
    }

    @Override
    public void annulerDerniereOperationSolde() {
        // Cette logique est simplifiée : elle annule la dernière commande de la pile
        // Pour annuler toute une campagne, on pourrait boucler
        gestionnaireSolde.annulerDerniereSolde();
    }

    @Override
    public List<VehiculeResultDTO> listerVehiculesEnSolde() {
        List<Vehicule> vehiculesEnSolde = vehiculeRepository.findAll()
                .stream()
                .filter(v -> v.getPourcentageSolde() != null && v.getPourcentageSolde() > 0)
                .toList();

        List<VehiculeResultDTO> results = new ArrayList<>();
        for (Vehicule v : vehiculesEnSolde) {
            results.add(vehiculeMapper.toDto(v));
        }
        return results;
    }

    @Override
    public List<VehiculeResultDTO> listerVehiculesSoldables() {
        List<Vehicule> vehiculesSoldables = vehiculeRepository.findAll()
                .stream()
                .filter(v -> v.getPourcentageSolde() == null || v.getPourcentageSolde() == 0)
                .toList();

        List<VehiculeResultDTO> results = new ArrayList<>();
        for (Vehicule v : vehiculesSoldables) {
            results.add(vehiculeMapper.toDto(v));
        }
        return results;
    }

    @Override
    public VehiculeResultDTO solderVehicule(Long vehiculeId, Double pourcentage) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule ID " + vehiculeId + " introuvable"));

        // Appliquer le solde via la commande
        SoldeCommand commande = new SoldeCommand(vehicule, pourcentage);
        gestionnaireSolde.executerCommande(commande);

        // Stocker le pourcentage de solde
        vehicule.setPourcentageSolde(pourcentage);

        Vehicule vehiculeUpdated = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(vehiculeUpdated);
    }

    @Override
    public void supprimerVehicule(Long vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule ID " + vehiculeId + " introuvable"));

        // Supprimer l'image associée
        if (vehicule.getImageLink() != null) {
            imageService.supprimerImage(vehicule.getImageLink());
        }

        // Supprimer le véhicule de la base de données
        vehiculeRepository.delete(vehicule);
        log.info("Véhicule ID {} supprimé avec succès", vehiculeId);
    }
}