package com.gestion_voiture.gestionnaire.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.SoldeRequestDTO;
import com.gestion_voiture.gestionnaire.services.VehiculeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/stock")
@Tag(name = "Stock", description = "Gestion des soldes")
@RequiredArgsConstructor
public class StockController {

    private final VehiculeService vehiculeService;

    @PostMapping("/solder")
    @Operation(summary = "Solder les véhicules via un DTO")
    public ResponseEntity<String> solderStock(@RequestBody SoldeRequestDTO dto) {
        vehiculeService.solderStock(dto);
        return ResponseEntity.ok("Campagne de solde de " + dto.getPourcentage() + "% appliquée.");
    }

    @PostMapping("/annuler")
    @Operation(summary = "Annuler la dernière opération de solde")
    public ResponseEntity<String> annuler() {
        vehiculeService.annulerDerniereOperationSolde();
        return ResponseEntity.ok("Annulation effectuée.");
    }
}