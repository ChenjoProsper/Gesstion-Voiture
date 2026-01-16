package com.gestion_voiture.gestionnaire.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.FormulaireRequestDTO;
import com.gestion_voiture.gestionnaire.services.FormulaireService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/formulaires")
@Tag(name = "Formulaires", description = "Gestion des Forumulaires")
@RequiredArgsConstructor
public class FormulaireController {

    private final FormulaireService formulaireService;

    @PostMapping("/generer")
    @Operation(summary = "Générer un formulaire (Immatriculation) en format HTML ou Widget")
    public String generer(@RequestBody FormulaireRequestDTO dto) {
        return formulaireService.genererFormulaire(dto);
    }
}