package com.gestion_voiture.gestionnaire.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;
import com.gestion_voiture.gestionnaire.services.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/commande")
@Tag(name="commande", description = "gestion des commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    @Operation(summary = "passer une commande (initialement EN_COURS)")
    public CommandeResultDTO passerCommande(@RequestBody CommandeDTO dto) {
        return commandeService.passerCommande(dto);
    }

    @PutMapping("/{id}/valider")
    @Operation(summary = "valider une commande (passe à l'état VALIDE et génère la liasse de documents)")
    public String valider(@PathVariable Long id) {
        commandeService.validerCommande(id);
        return "Commande validée et liasse générée.";
    }

    @PostMapping("/{id}/prix-final")
    public Double calculerTotal(
            @PathVariable Long id, 
            @RequestBody Map<Long, List<Long>> vehiculesOptions) {
        return commandeService.calculerPrixFinal(id, vehiculesOptions);
    }
}