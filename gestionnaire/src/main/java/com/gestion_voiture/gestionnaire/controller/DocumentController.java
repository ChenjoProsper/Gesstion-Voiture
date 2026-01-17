package com.gestion_voiture.gestionnaire.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.DocumentHtmlDTO;
import com.gestion_voiture.gestionnaire.dto.DocumentPdfDTO;
import com.gestion_voiture.gestionnaire.models.Document;
import com.gestion_voiture.gestionnaire.repository.DocumentRepository;
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
    private final DocumentRepository documentRepository;

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

    @GetMapping("/{documentId}")
    @Operation(summary = "Récupérer un document par son ID")
    public ResponseEntity<Document> obtenirDocument(@PathVariable Long documentId) {
        return documentRepository.findById(documentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Récupérer tous les documents d'un client")
    public ResponseEntity<List<Document>> obtenirDocumentsClient(@PathVariable Long clientId) {
        List<Document> documents = documentRepository.findByClientId(clientId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/client/{clientId}/titre/{titre}")
    @Operation(summary = "Récupérer les documents d'un client par titre")
    public ResponseEntity<List<Document>> obtenirDocumentsClientParTitre(
            @PathVariable Long clientId,
            @PathVariable String titre) {
        List<Document> documents = documentRepository.findByClientIdAndTitreContaining(clientId, titre);
        return ResponseEntity.ok(documents);
    }
}