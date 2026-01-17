package com.gestion_voiture.gestionnaire.services;

import java.util.List;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.dto.RechercheDTO;

public interface RechercheService {
    /**
     * Recherche simple par mot-clé
     */
    List<VehiculeResultDTO> rechercherParMotCle(String motCle);
    
    /**
     * Recherche avancée avec opérateurs ET/OU
     */
    List<VehiculeResultDTO> rechercherAvancee(RechercheDTO recherche);
    
    /**
     * Recherche par marque
     */
    List<VehiculeResultDTO> rechercherParMarque(String marque);
    
    /**
     * Recherche avec filtres multiples
     */
    List<VehiculeResultDTO> appliquerFiltres(RechercheDTO recherche);
    
    /**
     * Recherche les automobiles avec opérateurs ET/OU
     * Type fixé à AUTOMOBILE dans les résultats
     */
    List<VehiculeResultDTO> rechercherAutomobiles(RechercheDTO recherche);
    
    /**
     * Recherche les scooters avec opérateurs ET/OU
     * Type fixé à SCOOTER dans les résultats
     */
    List<VehiculeResultDTO> rechercherScooters(RechercheDTO recherche);
    
    /**
     * Route la recherche en fonction du type de véhicule
     */
    List<VehiculeResultDTO> rechercherParType(RechercheDTO recherche);
}

