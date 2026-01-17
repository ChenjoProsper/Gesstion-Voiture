package com.gestion_voiture.gestionnaire.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.dto.RechercheDTO;
import com.gestion_voiture.gestionnaire.mapper.VehiculeMapper;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.models.Automobile;
import com.gestion_voiture.gestionnaire.models.Scooter;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.repository.AutomobileRepository;
import com.gestion_voiture.gestionnaire.repository.ScooterRepository;
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
        String motCleNormalise = motCle.toLowerCase().trim();
        
        return vehiculeRepository.findAll().stream()
                .filter(v -> contientMotCle(v, motCleNormalise))
                .map(vehiculeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<VehiculeResultDTO> rechercherAvancee(RechercheDTO recherche) {
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        
        if (recherche.getMotsCles() == null || recherche.getMotsCles().isEmpty()) {
            return vehicules.stream()
                    .map(vehiculeMapper::toDto)
                    .collect(Collectors.toList());
        }
        
        String operateur = recherche.getOperateur() != null ? recherche.getOperateur().toUpperCase() : "OU";
        
        List<VehiculeResultDTO> resultats;
        
        if ("ET".equals(operateur)) {
            // Tous les mots-clés doivent être présents
            resultats = vehicules.stream()
                    .filter(v -> recherche.getMotsCles().stream()
                            .allMatch(mot -> contientMotCle(v, mot.toLowerCase())))
                    .map(vehiculeMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            // Au moins un mot-clé doit être présent (OU)
            resultats = vehicules.stream()
                    .filter(v -> recherche.getMotsCles().stream()
                            .anyMatch(mot -> contientMotCle(v, mot.toLowerCase())))
                    .map(vehiculeMapper::toDto)
                    .collect(Collectors.toList());
        }
        
        return resultats;
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
        
        // Filtrer par marque
        if (recherche.getMarque() != null && !recherche.getMarque().isEmpty()) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getMarque().equalsIgnoreCase(recherche.getMarque()))
                    .collect(Collectors.toList());
        }
        
        // Filtrer par modèle
        if (recherche.getModele() != null && !recherche.getModele().isEmpty()) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getModele().equalsIgnoreCase(recherche.getModele()))
                    .collect(Collectors.toList());
        }
        
        // Filtrer par prix
        if (recherche.getPrixMin() != null) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getPrixBase() >= recherche.getPrixMin())
                    .collect(Collectors.toList());
        }
        
        if (recherche.getPrixMax() != null) {
            vehicules = vehicules.stream()
                    .filter(v -> v.getPrixBase() <= recherche.getPrixMax())
                    .collect(Collectors.toList());
        }
        
        // Appliquer la recherche par mots-clés si fournis
        if (recherche.getMotsCles() != null && !recherche.getMotsCles().isEmpty()) {
            String operateur = recherche.getOperateur() != null ? recherche.getOperateur().toUpperCase() : "OU";
            
            if ("ET".equals(operateur)) {
                vehicules = vehicules.stream()
                        .filter(v -> recherche.getMotsCles().stream()
                                .allMatch(mot -> contientMotCle(v, mot.toLowerCase())))
                        .collect(Collectors.toList());
            } else {
                vehicules = vehicules.stream()
                        .filter(v -> recherche.getMotsCles().stream()
                                .anyMatch(mot -> contientMotCle(v, mot.toLowerCase())))
                        .collect(Collectors.toList());
            }
        }
        
        return vehicules.stream()
                .map(vehiculeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<VehiculeResultDTO> rechercherAutomobiles(RechercheDTO recherche) {
        List<Automobile> automobiles = automobileRepository.findAll();
        
        // Si pas de mots-clés, retourner tous les automobiles
        if (recherche.getMotsCles() == null || recherche.getMotsCles().isEmpty()) {
            return automobiles.stream()
                    .map(a -> {
                        VehiculeResultDTO dto = vehiculeMapper.toDto(a);
                        // Le typeVehicule est déjà défini par le mapper
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
        
        String operateur = recherche.getOperateur() != null ? recherche.getOperateur().toUpperCase() : "OU";
        
        List<VehiculeResultDTO> resultats;
        
        if ("ET".equals(operateur)) {
            // Tous les mots-clés doivent être présents
            resultats = automobiles.stream()
                    .filter(a -> recherche.getMotsCles().stream()
                            .allMatch(mot -> contientMotCle(a, mot.toLowerCase())))
                    .map(a -> {
                        VehiculeResultDTO dto = vehiculeMapper.toDto(a);
                        return dto;
                    })
                    .collect(Collectors.toList());
        } else {
            // Au moins un mot-clé doit être présent (OU)
            resultats = automobiles.stream()
                    .filter(a -> recherche.getMotsCles().stream()
                            .anyMatch(mot -> contientMotCle(a, mot.toLowerCase())))
                    .map(a -> {
                        VehiculeResultDTO dto = vehiculeMapper.toDto(a);
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
        
        return resultats;
    }
    
    @Override
    public List<VehiculeResultDTO> rechercherScooters(RechercheDTO recherche) {
        List<Scooter> scooters = scooterRepository.findAll();
        
        // Si pas de mots-clés, retourner tous les scooters
        if (recherche.getMotsCles() == null || recherche.getMotsCles().isEmpty()) {
            return scooters.stream()
                    .map(s -> {
                        VehiculeResultDTO dto = vehiculeMapper.toDto(s);
                        // Le typeVehicule est déjà défini par le mapper
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
        
        String operateur = recherche.getOperateur() != null ? recherche.getOperateur().toUpperCase() : "OU";
        
        List<VehiculeResultDTO> resultats;
        
        if ("ET".equals(operateur)) {
            // Tous les mots-clés doivent être présents
            resultats = scooters.stream()
                    .filter(s -> recherche.getMotsCles().stream()
                            .allMatch(mot -> contientMotCle(s, mot.toLowerCase())))
                    .map(s -> {
                        VehiculeResultDTO dto = vehiculeMapper.toDto(s);
                        return dto;
                    })
                    .collect(Collectors.toList());
        } else {
            // Au moins un mot-clé doit être présent (OU)
            resultats = scooters.stream()
                    .filter(s -> recherche.getMotsCles().stream()
                            .anyMatch(mot -> contientMotCle(s, mot.toLowerCase())))
                    .map(s -> {
                        VehiculeResultDTO dto = vehiculeMapper.toDto(s);
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
        
        return resultats;
    }
    
    @Override
    public List<VehiculeResultDTO> rechercherParType(RechercheDTO recherche) {
        if (recherche.getTypeVehicule() == null || recherche.getTypeVehicule().isEmpty()) {
            throw new IllegalArgumentException("Type de véhicule obligatoire (AUTOMOBILE ou SCOOTER)");
        }
        
        String type = recherche.getTypeVehicule().toUpperCase();
        
        switch (type) {
            case "AUTOMOBILE":
                return rechercherAutomobiles(recherche);
            case "SCOOTER":
                return rechercherScooters(recherche);
            default:
                throw new IllegalArgumentException("Type de véhicule invalide: " + type + ". Doit être AUTOMOBILE ou SCOOTER");
        }
    }
    
    /**
     * Vérifie si un véhicule contient un mot-clé dans ses attributs
     */
    private boolean contientMotCle(Vehicule vehicule, String motCle) {
        return (vehicule.getMarque() != null && vehicule.getMarque().toLowerCase().contains(motCle)) ||
               (vehicule.getModele() != null && vehicule.getModele().toLowerCase().contains(motCle)) ||
               (vehicule.getDescription() != null && vehicule.getDescription().toLowerCase().contains(motCle)) ||
               (vehicule.getReference() != null && vehicule.getReference().toLowerCase().contains(motCle));
    }
}
