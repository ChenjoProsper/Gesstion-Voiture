package com.gestion_voiture.gestionnaire.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.services.VehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/vehicules")
@Tag(name="vehicules", description = "gestion des vehicules")
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
}