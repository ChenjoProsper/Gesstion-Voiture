package com.gestion_voiture.gestionnaire.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_voiture.gestionnaire.models.Panier;
import com.gestion_voiture.gestionnaire.services.PanierService;

@RestController
@RequestMapping("/api/paniers")
public class PanierController {

    private final PanierService panierService;

    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Panier> getPanier(@PathVariable Long clientId) {
        return ResponseEntity.ok(panierService.getPanierByClient(clientId));
    }

    @PostMapping("/client/{clientId}/items")
    public ResponseEntity<Panier> addItem(@PathVariable Long clientId,
                                          @RequestParam Long vehiculeId,
                                          @RequestBody(required = false) List<Long> optionIds,
                                          @RequestParam(required = false) Integer quantite) {
        return ResponseEntity.ok(panierService.addItem(clientId, vehiculeId, optionIds, quantite));
    }

    @DeleteMapping("/client/{clientId}/items/{itemId}")
    public ResponseEntity<Panier> removeItem(@PathVariable Long clientId, @PathVariable Long itemId) {
        return ResponseEntity.ok(panierService.removeItem(clientId, itemId));
    }

    @PostMapping("/client/{clientId}/clear")
    public ResponseEntity<Panier> clear(@PathVariable Long clientId) {
        return ResponseEntity.ok(panierService.clearCart(clientId));
    }

    @PostMapping("/client/{clientId}/undo")
    public ResponseEntity<Void> undo(@PathVariable Long clientId) {
        boolean ok = panierService.undo(clientId);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
