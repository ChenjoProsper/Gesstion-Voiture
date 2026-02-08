package com.gestion_voiture.gestionnaire.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.RechercheDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.mapper.VehiculeMapper;
import com.gestion_voiture.gestionnaire.models.Automobile;
import com.gestion_voiture.gestionnaire.models.Scooter;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.repository.AutomobileRepository;
import com.gestion_voiture.gestionnaire.repository.ScooterRepository;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.services.RechercheService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechercheServiceImpl implements RechercheService {

    private final VehiculeRepository vehiculeRepository;
    private final AutomobileRepository automobileRepository;
    private final ScooterRepository scooterRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public List<VehiculeResultDTO> rechercherParMotCle(String motCle) {
        if (motCle == null || motCle.isEmpty()) return List.of();
        String motNormalise = motCle.toLowerCase().trim();
        return vehiculeRepository.searchByKeyword(motNormalise).stream()
                .map(vehiculeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculeResultDTO> rechercherAvancee(RechercheDTO recherche) {
        if (recherche.getMotsCles() == null || recherche.getMotsCles().isEmpty()) {
            return vehiculeRepository.findAll().stream()
                    .map(vehiculeMapper::toDto)
                    .collect(Collectors.toList());
        }

        String operateur = recherche.getOperateur() != null ? recherche.getOperateur().toUpperCase() : "OU";
        List<Vehicule> accumulated = new ArrayList<>();

        for (String mot : recherche.getMotsCles()) {
            String m = mot.toLowerCase().trim();
            List<Vehicule> found = vehiculeRepository.searchByKeyword(m);
            if (accumulated.isEmpty()) {
                accumulated.addAll(found);
            } else {
                if ("ET".equals(operateur)) {
                    accumulated.retainAll(found);
                } else {
                    for (Vehicule v : found) {
                        if (!accumulated.contains(v)) {
                            accumulated.add(v);
                        }
                    }
                }
            }
        }

        return accumulated.stream().map(vehiculeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<VehiculeResultDTO> rechercherParMarque(String marque) {
        return vehiculeRepository.findByMarque(marque).stream()
                .map(vehiculeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehiculeResultDTO> appliquerFiltres(RechercheDTO recherche) {
        List<Vehicule> vehicules = vehiculeRepository.findAll();

        if (recherche.getMarque() != null && !recherche.getMarque().isEmpty()) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getMarque() != null && v.getMarque().equalsIgnoreCase(recherche.getMarque()))
                    .collect(Collectors.toList());
        }

        if (recherche.getModele() != null && !recherche.getModele().isEmpty()) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getModele() != null && v.getModele().equalsIgnoreCase(recherche.getModele()))
                    .collect(Collectors.toList());
        }

        if (recherche.getPrixMin() != null) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getPrixBase() != null && v.getPrixBase() >= recherche.getPrixMin())
                    .collect(Collectors.toList());
        }

        if (recherche.getPrixMax() != null) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getPrixBase() != null && v.getPrixBase() <= recherche.getPrixMax())
                    .collect(Collectors.toList());
        }

        // Filtre par mots-clés
        if (recherche.getMotsCles() != null && !recherche.getMotsCles().isEmpty()) {
            String operateur = recherche.getOperateur() != null ? recherche.getOperateur().toUpperCase() : "OU";
            if ("ET".equals(operateur)) {
                vehicules = vehicules.stream()
                        .filter(v -> recherche.getMotsCles().stream().allMatch(m -> contientMotCle(v, m.toLowerCase())))
                        .collect(Collectors.toList());
            } else {
                vehicules = vehicules.stream()
                        .filter(v -> recherche.getMotsCles().stream().anyMatch(m -> contientMotCle(v, m.toLowerCase())))
                        .collect(Collectors.toList());
            }
        }

        return vehicules.stream().map(vehiculeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<VehiculeResultDTO> rechercherAutomobiles(RechercheDTO recherche) {
        List<Automobile> autos = automobileRepository.findAll();
        return filterByMotsCles(autos, recherche);
    }

    @Override
    public List<VehiculeResultDTO> rechercherScooters(RechercheDTO recherche) {
        List<Scooter> scooters = scooterRepository.findAll();
        return filterByMotsCles(scooters, recherche);
    }

    @Override
    public List<VehiculeResultDTO> rechercherParType(RechercheDTO recherche) {
        if (recherche.getTypeVehicule() == null || recherche.getTypeVehicule().isEmpty())
            throw new IllegalArgumentException("Type de véhicule obligatoire (AUTOMOBILE ou SCOOTER)");

        String type = recherche.getTypeVehicule().toUpperCase();
        switch (type) {
            case "AUTOMOBILE":
                List<Automobile> autos = new ArrayList<>();
                autos.addAll(vehiculeRepository.findAllAutoEssence());
                autos.addAll(vehiculeRepository.findAllAutoElectrique());
                return filterByMotsCles(autos, recherche);
            case "SCOOTER":
                List<Scooter> scooters = new ArrayList<>();
                scooters.addAll(vehiculeRepository.findAllScooterEssence());
                scooters.addAll(vehiculeRepository.findAllScooterElectrique());
                return filterByMotsCles(scooters, recherche);
            default:
                throw new IllegalArgumentException("Type de véhicule invalide: " + type + ". Doit être AUTOMOBILE ou SCOOTER");
        }
    }

    // Méthode générique de filtrage par mots-clés
    private <T extends Vehicule> List<VehiculeResultDTO> filterByMotsCles(List<T> vehicules, RechercheDTO recherche) {
        if (recherche.getMotsCles() == null || recherche.getMotsCles().isEmpty()) {
            return vehicules.stream().map(vehiculeMapper::toDto).collect(Collectors.toList());
        }

        String operateur = recherche.getOperateur() != null ? recherche.getOperateur().toUpperCase() : "OU";
        List<T> filtered;

        if ("ET".equals(operateur)) {
            filtered = vehicules.stream()
                    .filter(v -> recherche.getMotsCles().stream().allMatch(m -> contientMotCle(v, m.toLowerCase())))
                    .collect(Collectors.toList());
        } else {
            filtered = vehicules.stream()
                    .filter(v -> recherche.getMotsCles().stream().anyMatch(m -> contientMotCle(v, m.toLowerCase())))
                    .collect(Collectors.toList());
        }

        return filtered.stream().map(vehiculeMapper::toDto).collect(Collectors.toList());
    }

    // Vérifie si un véhicule contient un mot-clé
    private boolean contientMotCle(Vehicule vehicule, String motCle) {
        if (vehicule == null || motCle == null) return false;
        return (vehicule.getMarque() != null && vehicule.getMarque().toLowerCase().contains(motCle)) ||
               (vehicule.getModele() != null && vehicule.getModele().toLowerCase().contains(motCle)) ||
               (vehicule.getDescription() != null && vehicule.getDescription().toLowerCase().contains(motCle)) ||
               (vehicule.getReference() != null && vehicule.getReference().toLowerCase().contains(motCle));
    }
}
