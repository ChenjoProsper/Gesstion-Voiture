package com.gestion_voiture.gestionnaire.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.dto.RechercheDTO;
import com.gestion_voiture.gestionnaire.services.RechercheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recherche")
@Tag(name = "Recherche", description = "Recherche avancée de véhicules avec opérateurs logiques par type")
@RequiredArgsConstructor
public class RechercheController {
    
    private final RechercheService rechercheService;
    
    @GetMapping("/simple")
    @Operation(summary = "Recherche simple par mot-clé")
    public List<VehiculeResultDTO> rechercherSimple(@RequestParam String motCle) {
        return rechercheService.rechercherParMotCle(motCle);
    }
    
    @PostMapping("/avancee")
    @Operation(summary = "Recherche avancée avec opérateurs ET/OU")
    public List<VehiculeResultDTO> rechercherAvancee(@RequestBody RechercheDTO rechercheDTO) {
        return rechercheService.rechercherAvancee(rechercheDTO);
    }
    
    @GetMapping("/marque/{marque}")
    @Operation(summary = "Recherche par marque")
    public List<VehiculeResultDTO> rechercherParMarque(@RequestParam String marque) {
        return rechercheService.rechercherParMarque(marque);
    }
    
    @PostMapping("/filtres")
    @Operation(summary = "Recherche avec filtres multiples")
    public List<VehiculeResultDTO> rechercherAvecFiltres(@RequestBody RechercheDTO rechercheDTO) {
        return rechercheService.appliquerFiltres(rechercheDTO);
    }
    
    // ============ NOUVEAUX ENDPOINTS ALIGNÉS DB ============
    
    /**
     * Recherche les AUTOMOBILES uniquement avec opérateurs ET/OU
     * Endpoint: POST /api/recherche/automobiles
     * Type toujours fixé à "AUTOMOBILE" dans les résultats
     */
    @PostMapping("/automobiles")
    @Operation(summary = "Rechercher les AUTOMOBILES avec opérateurs ET/OU")
    public ResponseEntity<List<VehiculeResultDTO>> rechercherAutomobiles(@RequestBody RechercheDTO recherche) {
        return ResponseEntity.ok(rechercheService.rechercherAutomobiles(recherche));
    }
    
    /**
     * Recherche les SCOOTERS uniquement avec opérateurs ET/OU
     * Endpoint: POST /api/recherche/scooters
     * Type toujours fixé à "SCOOTER" dans les résultats
     */
    @PostMapping("/scooters")
    @Operation(summary = "Rechercher les SCOOTERS avec opérateurs ET/OU")
    public ResponseEntity<List<VehiculeResultDTO>> rechercherScooters(@RequestBody RechercheDTO recherche) {
        return ResponseEntity.ok(rechercheService.rechercherScooters(recherche));
    }
    
    /**
     * Recherche routée par type de véhicule
     * Le frontend doit spécifier typeVehicule = "AUTOMOBILE" ou "SCOOTER"
     * Endpoint: POST /api/recherche?type=AUTOMOBILE
     * ou
     * Endpoint: POST /api/recherche
     * Avec body: { "typeVehicule": "AUTOMOBILE", "motsCles": [...], "operateur": "ET" }
     */
    @PostMapping("")
    @Operation(summary = "Recherche routée par type (AUTOMOBILE ou SCOOTER)")
    public ResponseEntity<List<VehiculeResultDTO>> rechercherParType(@RequestBody RechercheDTO recherche) {
        return ResponseEntity.ok(rechercheService.rechercherParType(recherche));
    }
    
    @PostMapping("/type")
    @Operation(summary = "Recherche routée par type avec query param (alternative)")
    public ResponseEntity<List<VehiculeResultDTO>> rechercherParTypeParam(
            @RequestParam String type,
            @RequestBody RechercheDTO recherche) {
        recherche.setTypeVehicule(type);
        return ResponseEntity.ok(rechercheService.rechercherParType(recherche));
    }
}
