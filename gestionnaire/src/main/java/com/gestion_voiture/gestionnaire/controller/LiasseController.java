package com.gestion_voiture.gestionnaire.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.LiasseResultDTO;
import com.gestion_voiture.gestionnaire.services.LiasseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/liasse")
@Tag(name = "Liasse", description = "Gestion de la liasse")
@RequiredArgsConstructor
public class LiasseController {
    private final LiasseService liasseService;

    @GetMapping("/vierge")
    @Operation(summary = "Récupérer l'instance unique de la liasse vierge")
    public ResponseEntity<LiasseResultDTO> getVierge() {
        return ResponseEntity.ok(liasseService.getLiasseVierge());
    }
}