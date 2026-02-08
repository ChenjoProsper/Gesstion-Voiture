package com.gestion_voiture.gestionnaire.services;

import java.util.List;

import com.gestion_voiture.gestionnaire.models.Panier;

public interface PanierService {
    Panier getPanierByClient(Long clientId);
    Panier addItem(Long clientId, Long vehiculeId, List<Long> optionIds, Integer quantite);
    Panier removeItem(Long clientId, Long panierItemId);
    Panier clearCart(Long clientId);
    boolean undo(Long clientId);
}
