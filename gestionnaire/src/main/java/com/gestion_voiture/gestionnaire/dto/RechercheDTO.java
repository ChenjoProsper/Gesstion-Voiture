package com.gestion_voiture.gestionnaire.dto;

import java.util.List;
import lombok.Data;

/**
 * DTO pour les recherches avancées avec opérateurs logiques
 */
@Data
public class RechercheDTO {
    private List<String> motsCles;
    
    /**
     * Type d'opérateur : "ET" ou "OU"
     * ET = tous les mots-clés doivent être présents
     * OU = au moins un mot-clé doit être présent
     */
    private String operateur = "OU";
    
    private String marque;
    private String modele;
    private Double prixMin;
    private Double prixMax;
    private String typeVehicule;
    
    /**
     * Champs pour la recherche
     * "marque", "modele", "description", "titre"
     */
    private List<String> champsRecherche;
}
