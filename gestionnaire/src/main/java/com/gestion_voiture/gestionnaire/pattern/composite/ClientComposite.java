package com.gestion_voiture.gestionnaire.pattern.composite;

import java.util.List;

/**
 * Pattern Composite pour représenter les clients et les sociétés avec filiales
 */
public interface ClientComposite {
    /**
     * Retourne le nom du client/société
     */
    String getNom();
    
    /**
     * Ajoute une filiale ou un client enfant
     */
    void addClient(ClientComposite client);
    
    /**
     * Retire une filiale ou un client enfant
     */
    void removeClient(ClientComposite client);
    
    /**
     * Retourne la liste des clients enfants
     */
    List<ClientComposite> getClients();
    
    /**
     * Affiche la structure hiérarchique
     */
    void afficher(int niveau);
    
    /**
     * Compte le nombre total de clients (y compris les filiales)
     */
    int compterClients();
}
