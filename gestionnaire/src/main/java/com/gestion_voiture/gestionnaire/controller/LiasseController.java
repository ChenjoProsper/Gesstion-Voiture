package com.gestion_voiture.gestionnaire.controller;

import com.gestion_voiture.gestionnaire.dto.LiasseDTO;
import com.gestion_voiture.gestionnaire.services.LiasseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/liasse")
@Tag(name="liasses", description = "gestion des liasses de documents")
@RequiredArgsConstructor
public class LiasseController {

    private final LiasseService liasseService;

    @GetMapping("/vierge")
    @Operation(summary = "Récupère l'unique instance de la liasse vierge (Singleton)")
    public ResponseEntity<LiasseDTO> getLiasseVierge() {
        return ResponseEntity.ok(liasseService.obtenirLiasseVierge());
    }
}