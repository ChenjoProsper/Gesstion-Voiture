package com.gestion_voiture.gestionnaire.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.DocumentHtmlDTO;
import com.gestion_voiture.gestionnaire.dto.DocumentPdfDTO;
import com.gestion_voiture.gestionnaire.models.Document;
import com.gestion_voiture.gestionnaire.services.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/documents")
@Tag(name = "Documents", description = "Gestion des documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/pdf")
    @Operation(summary = "Créer un document PDF via l'Adapter")
    public ResponseEntity<Document> creerPdf(@RequestBody DocumentPdfDTO dto) {
        return ResponseEntity.ok(documentService.creerDocumentPdf(dto));
    }

    @PostMapping("/html")
    @Operation(summary = "Créer un document HTML via l'Adapter")
    public ResponseEntity<Document> creerHtml(@RequestBody DocumentHtmlDTO dto) {
        return ResponseEntity.ok(documentService.creerDocumentHtml(dto));
    }
}