package com.gestion_voiture.gestionnaire.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.ClientDTO;
import com.gestion_voiture.gestionnaire.dto.ClientResultDTO;
import com.gestion_voiture.gestionnaire.services.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/client")
@Tag(name="clients", description = "gestion des clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "creer un client")
    public ResponseEntity<ClientResultDTO> creerClient(@Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.creerClient(dto));
    }

    @GetMapping
    @Operation(summary = "afficher les clients de la bd")
    public ResponseEntity<List<ClientResultDTO>> listerClients() {
        return ResponseEntity.ok(clientService.listerTousLesClients());
    }
}
