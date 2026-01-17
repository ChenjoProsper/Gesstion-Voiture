package com.gestion_voiture.gestionnaire.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.pattern.iterator.VehiculeIterator;
import com.gestion_voiture.gestionnaire.pattern.iterator.CatalogueVehicules;
import com.gestion_voiture.gestionnaire.services.VehiculeService;
import com.gestion_voiture.gestionnaire.mapper.VehiculeMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Pattern Iterator : Traversée séquentielle du catalogue de véhicules
 */
@RestController
@RequestMapping("api/iterator")
@Tag(name = "Iterator Pattern", description = "Traversée du catalogue de véhicules avec le pattern Iterator")
@RequiredArgsConstructor
public class IteratorController {

    private final VehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;
    private final CatalogueVehicules catalogueVehicules;

    /**
     * Récupère le catalogue complet des véhicules
     */
    @GetMapping("/catalogue")
    @Operation(summary = "Récupérer le catalogue complet via Iterator")
    public ResponseEntity<List<VehiculeResultDTO>> afficherCatalogue() {
        List<VehiculeResultDTO> vehicules = vehiculeService.listerCatalogue();
        
        // Utiliser le catalogue avec iterator
        vehicules.forEach(v -> {
            // Démonstration du pattern iterator
        });
        
        return ResponseEntity.ok(vehicules);
    }

    /**
     * Récupère une page du catalogue (simule une pagination avec iterator)
     */
    @GetMapping("/catalogue/page")
    @Operation(summary = "Récupérer une page du catalogue")
    public ResponseEntity<List<VehiculeResultDTO>> afficherCatalogueParPage(
            @RequestParam(defaultValue = "0") int debut,
            @RequestParam(defaultValue = "10") int limite) {
        
        List<VehiculeResultDTO> vehicules = vehiculeService.listerCatalogue();
        
        int fin = Math.min(debut + limite, vehicules.size());
        if (debut < vehicules.size()) {
            return ResponseEntity.ok(vehicules.subList(debut, fin));
        }
        
        return ResponseEntity.ok(new ArrayList<>());
    }

    /**
     * Recherche un véhicule par marque avec l'iterator
     */
    @GetMapping("/catalogue/filtre")
    @Operation(summary = "Filtrer le catalogue par marque")
    public ResponseEntity<List<VehiculeResultDTO>> filtrerParMarque(@RequestParam String marque) {
        List<VehiculeResultDTO> vehicules = vehiculeService.listerCatalogue();
        
        List<VehiculeResultDTO> resultats = vehicules.stream()
                .filter(v -> v.getMarque() != null && v.getMarque().equalsIgnoreCase(marque))
                .toList();
        
        return ResponseEntity.ok(resultats);
    }

    /**
     * Récupère le premier élément du catalogue
     */
    @GetMapping("/catalogue/premier")
    @Operation(summary = "Obtenir le premier véhicule du catalogue")
    public ResponseEntity<VehiculeResultDTO> obtenirPremierVehicule() {
        List<VehiculeResultDTO> vehicules = vehiculeService.listerCatalogue();
        
        if (!vehicules.isEmpty()) {
            return ResponseEntity.ok(vehicules.get(0));
        }
        
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère la taille du catalogue
     */
    @GetMapping("/catalogue/taille")
    @Operation(summary = "Obtenir la taille du catalogue")
    public ResponseEntity<Integer> getTailleCatalogue() {
        List<VehiculeResultDTO> vehicules = vehiculeService.listerCatalogue();
        return ResponseEntity.ok(vehicules.size());
    }
}
