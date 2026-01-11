package com.gestion_voiture.gestionnaire.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.services.VehiculeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/vehicules")
@Tag(name="vehicules", description = "gestion des vehicules")
@RequiredArgsConstructor
public class VehiculeController {
    private final VehiculeService vehiculeService;

    @PostMapping
    @Operation(summary = "ajouter un vehicule")
    public VehiculeResultDTO ajouter(@RequestBody VehiculeDTO dto) { 
        System.out.println("Received VehiculeDTO: " + dto);
        return vehiculeService.ajouterVehicule(dto); 
    }

    @GetMapping("/catalogue")
    @Operation(summary = "afficher le catalogue de vehicule")
    public List<VehiculeResultDTO> catalogue() {
        return vehiculeService.listerCatalogue();
    }
}
