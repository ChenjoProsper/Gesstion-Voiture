package com.gestion_voiture.gestionnaire.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.dto.CommandeDTO;
import com.gestion_voiture.gestionnaire.dto.CommandeResultDTO;
import com.gestion_voiture.gestionnaire.models.Commande;
import com.gestion_voiture.gestionnaire.models.CommandeComptant;
import com.gestion_voiture.gestionnaire.models.CommandeCredit;
import com.gestion_voiture.gestionnaire.pattern.templatemethod.CalculateurMontantComptant;
import com.gestion_voiture.gestionnaire.pattern.templatemethod.CalculateurMontantCredit;
import com.gestion_voiture.gestionnaire.services.CommandeService;
import com.gestion_voiture.gestionnaire.mapper.CommandeMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Pattern Template Method : Calcul du montant d'une commande avec différentes stratégies
 */
@RestController
@RequestMapping("api/template-method")
@Tag(name = "Template Method Pattern", description = "Calcul du montant des commandes avec différentes modes de paiement")
@RequiredArgsConstructor
public class TemplateMethodController {

    private final CommandeService commandeService;
    private final CommandeMapper commandeMapper;
    private final CalculateurMontantComptant calculateurComptant;
    private final CalculateurMontantCredit calculateurCredit;

    /**
     * Calcule le montant total d'une commande au comptant
     * Applique les étapes du template method : calcul base, réductions, frais, taxes, livraison
     */
    @PostMapping("/calcul-comptant")
    @Operation(summary = "Calculer le montant d'une commande au comptant (Template Method)")
    public ResponseEntity<CommandeResultDTO> calculerMontantComptant(@RequestBody CommandeDTO dto) {
        CommandeResultDTO result = commandeService.passerCommande(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * Calcule le montant total d'une commande à crédit
     * Applique les étapes du template method avec intérêts crédit
     */
    @PostMapping("/calcul-credit")
    @Operation(summary = "Calculer le montant d'une commande à crédit (Template Method)")
    public ResponseEntity<CommandeResultDTO> calculerMontantCredit(@RequestBody CommandeDTO dto) {
        CommandeResultDTO result = commandeService.passerCommande(dto);
        return ResponseEntity.ok(result);
    }

    /**
     * Détails du calcul du montant pour une commande
     */
    @PostMapping("/detail-calcul")
    @Operation(summary = "Obtenir le détail du calcul du montant")
    public ResponseEntity<String> detailCalcul(@RequestBody CommandeDTO dto) {
        StringBuilder detail = new StringBuilder();
        detail.append("=== DÉTAIL DU CALCUL (Template Method) ===\n");
        
        // Montant de base (simulé)
        double montantBase = 50000.00; // Exemple
        detail.append(String.format("1. Montant de base (véhicules): %.2f€\n", montantBase));
        
        // Réductions
        double reduction = montantBase * 0.05; // 5% de réduction au comptant, 0% au crédit
        detail.append(String.format("2. Réduction appliquée: %.2f€\n", reduction));
        
        // Frais spécifiques
        detail.append("3. Frais spécifiques: 0€ (exemple, peut varier)\n");
        
        // Taxes (19.2% Cameroun, 20% par défaut)
        double taxe = (montantBase - reduction) * 0.192;
        detail.append(String.format("4. Taxes (19.2%): %.2f€\n", taxe));
        
        // Frais de livraison
        double fraisLivraison = 50.00; // Exemple: 50€ au comptant
        detail.append(String.format("5. Frais de livraison: %.2f€\n", fraisLivraison));
        
        double montantTotal = montantBase - reduction + taxe + fraisLivraison;
        detail.append(String.format("\nMONTANT TOTAL: %.2f€\n", montantTotal));
        
        detail.append("\n=== ÉTAPES DU TEMPLATE METHOD ===\n");
        detail.append("1. Calculer le montant de base\n");
        detail.append("2. Appliquer les réductions\n");
        detail.append("3. Appliquer les frais spécifiques\n");
        detail.append("4. Calculer et ajouter les taxes\n");
        detail.append("5. Ajouter les frais de livraison\n");
        
        return ResponseEntity.ok(detail.toString());
    }
}
