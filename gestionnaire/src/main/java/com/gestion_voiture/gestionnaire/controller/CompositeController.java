package com.gestion_voiture.gestionnaire.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.ClientDTO;
import com.gestion_voiture.gestionnaire.dto.ClientResultDTO;
import com.gestion_voiture.gestionnaire.models.Client;
import com.gestion_voiture.gestionnaire.models.Societe;
import com.gestion_voiture.gestionnaire.pattern.composite.ClientSimple;
import com.gestion_voiture.gestionnaire.services.ClientService;
import com.gestion_voiture.gestionnaire.mapper.ClientMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Pattern Composite : Gestion des hiérarchies client (sociétés avec filiales)
 */
@RestController
@RequestMapping("api/composite")
@Tag(name = "Composite Pattern", description = "Gestion des hiérarchies client et des sociétés avec filiales")
@RequiredArgsConstructor
public class CompositeController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    /**
     * Crée une nouvelle sociéte (nœud composite)
     */
    @PostMapping("/societe")
    @Operation(summary = "Créer une nouvelle sociéte avec structure composite")
    public ResponseEntity<ClientResultDTO> creerSociete(@RequestBody ClientDTO dto) {
        ClientResultDTO result = clientService.creerClient(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * Ajoute une filiale à une sociéte (relation composite)
     */
    @PostMapping("/societe/{societeId}/filiale/{filialeId}")
    @Operation(summary = "Ajouter une filiale à une sociéte")
    public ResponseEntity<String> ajouterFiliale(
            @PathVariable Long societeId,
            @PathVariable Long filialeId) {
        
        try {
            clientService.ajouterFiliale(societeId, filialeId);
            return ResponseEntity.ok("Filiale ajoutée avec succès à la sociéte");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Récupère une sociéte avec toutes ses filiales (affichage hiérarchique)
     */
    @GetMapping("/societe/{societeId}")
    @Operation(summary = "Récupérer une sociéte et sa hiérarchie de filiales")
    public ResponseEntity<ClientResultDTO> afficherHierarchie(@PathVariable Long societeId) {
        Client client = clientService.findById(societeId);
        
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!(client instanceof Societe)) {
            return ResponseEntity.badRequest().build();
        }
        
        ClientResultDTO result = clientMapper.toDto(client);
        return ResponseEntity.ok(result);
    }

    /**
     * Compte le nombre total de clients dans la hiérarchie
     */
    @GetMapping("/societe/{societeId}/nombre-clients")
    @Operation(summary = "Compter le nombre total de clients dans la hiérarchie")
    public ResponseEntity<String> compterClients(@PathVariable Long societeId) {
        Client client = clientService.findById(societeId);
        
        if (client == null || !(client instanceof Societe)) {
            return ResponseEntity.badRequest().body("Client non trouvé ou pas une sociéte");
        }
        
        Societe societe = (Societe) client;
        int total = societe.compterClients();
        
        return ResponseEntity.ok(String.format(
            "La sociéte '%s' contient %d client(s) au total (y compris les filiales)",
            societe.getNom(), total
        ));
    }

    /**
     * Récupère les filiales directes d'une sociéte
     */
    @GetMapping("/societe/{societeId}/filiales")
    @Operation(summary = "Récupérer les filiales directes d'une sociéte")
    public ResponseEntity<List<ClientResultDTO>> obtenirFiliales(@PathVariable Long societeId) {
        Client client = clientService.findById(societeId);
        
        if (client == null || !(client instanceof Societe)) {
            return ResponseEntity.badRequest().build();
        }
        
        Societe societe = (Societe) client;
        List<ClientResultDTO> filiales = societe.getFiliales().stream()
                .map(clientMapper::toDto)
                .toList();
        
        return ResponseEntity.ok(filiales);
    }

    /**
     * Affiche la structure complète (pour debug/affichage)
     */
    @GetMapping("/societe/{societeId}/structure")
    @Operation(summary = "Afficher la structure hiérarchique complète")
    public ResponseEntity<String> afficherStructure(@PathVariable Long societeId) {
        Client client = clientService.findById(societeId);
        
        if (client == null || !(client instanceof Societe)) {
            return ResponseEntity.badRequest().body("Client non trouvé ou pas une sociéte");
        }
        
        Societe societe = (Societe) client;
        
        StringBuilder structure = new StringBuilder();
        structure.append("═══════════════════════════════════════════\n");
        structure.append("STRUCTURE ORGANISATIONNELLE\n");
        structure.append("═══════════════════════════════════════════\n");
        structure.append("Sociéte: ").append(societe.getNom()).append("\n");
        if (societe.getNumeroSIREN() != null) {
            structure.append("SIREN: ").append(societe.getNumeroSIREN()).append("\n");
        }
        if (societe.getAdresseSiege() != null) {
            structure.append("Adresse: ").append(societe.getAdresseSiege()).append("\n");
        }
        structure.append("Nombre total de clients: ").append(societe.compterClients()).append("\n");
        structure.append("═══════════════════════════════════════════\n");
        
        return ResponseEntity.ok(structure.toString());
    }
}
