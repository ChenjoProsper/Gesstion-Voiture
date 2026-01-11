package com.gestion_voiture.gestionnaire.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;
import com.gestion_voiture.gestionnaire.services.CommandeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/commande")
@Tag(name="commande", description = "gestion des commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    @Operation(summary = "passer une commande")
    public CommandeResultDTO passerCommande(@RequestBody CommandeDTO dto) {
        return commandeService.passerCommande(dto);
    }
}
