package com.gestion_voiture.gestionnaire.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.services.VehiculeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/vehicules")
@Tag(name = "vehicules", description = "gestion des vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    @Operation(summary = "ajouter un vehicule")
    public ResponseEntity<VehiculeResultDTO> ajouter(@RequestBody VehiculeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.ajouterVehicule(dto));
    }

    @GetMapping("/catalogue")
    @Operation(summary = "afficher le catalogue de vehicule (Pattern Iterator)")
    public ResponseEntity<List<VehiculeResultDTO>> catalogue() {
        return ResponseEntity.ok(vehiculeService.listerCatalogue());
    }

    @GetMapping("/{id}/calculer-prix")
    @Operation(summary = "Calculer le prix réel avec options ")
    public ResponseEntity<Double> calculerPrixOption(
            @PathVariable Long id,
            @RequestParam List<Long> optionsIds) {
        // Cette méthode dans le service utilise l'OptionDecorateur
        return ResponseEntity.ok(vehiculeService.calculerPrixReel(id, optionsIds));
    }

    @GetMapping("/soldes")
    @Operation(summary = "Récupérer tous les véhicules en solde")
    public ResponseEntity<List<VehiculeResultDTO>> listerVehiculesEnSolde() {
        return ResponseEntity.ok(vehiculeService.listerVehiculesEnSolde());
    }

    @GetMapping("/soldables")
    @Operation(summary = "Afficher les véhicules soldables (non encore en solde)")
    public ResponseEntity<List<VehiculeResultDTO>> listerVehiculesSoldables() {
        return ResponseEntity.ok(vehiculeService.listerVehiculesSoldables());
    }

    @PutMapping("/{id}/solder")
    @Operation(summary = "Solder un véhicule en particulier avec un pourcentage de réduction")
    public ResponseEntity<VehiculeResultDTO> solderVehicule(
            @PathVariable Long id,
            @RequestParam Double pourcentage) {
        return ResponseEntity.ok(vehiculeService.solderVehicule(id, pourcentage));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un véhicule et son image associée")
    public ResponseEntity<Void> supprimerVehicule(@PathVariable Long id) {
        vehiculeService.supprimerVehicule(id);
        return ResponseEntity.noContent().build();
    }
}