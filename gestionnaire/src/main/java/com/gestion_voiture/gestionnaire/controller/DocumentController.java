package com.gestion_voiture.gestionnaire.controller;

import com.gestion_voiture.gestionnaire.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/document")
@Tag(name="documents", description = "Gestion des documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/imprimer-pdf")
    @Operation(summary = "Génère et imprime un document via l'Adapter PDF")
    public ResponseEntity<String> imprimerPdf(@RequestBody String contenu) {
        return ResponseEntity.ok(documentService.genererEtImprimerPdf(contenu));
    }
}