package com.gestion_voiture.gestionnaire.services;

import java.io.IOException;

public interface ImageService {
    /**
     * Sauvegarde une image en base64 et retourne le lien relatif
     * 
     * @param base64Image Image en base64 (avec ou sans préfixe "data:image/...")
     * @param vehiculeId  ID unique du véhicule pour nommer le fichier
     * @return Le lien relatif de l'image (ex: /uploads/vehicules/vehicule-1.jpg)
     */
    String sauvegarderImage(String base64Image, Long vehiculeId) throws IOException;

    /**
     * Supprime le fichier image associé au lien
     * 
     * @param imageLink Le lien relatif de l'image (ex:
     *                  /uploads/vehicules/vehicule-1.jpg)
     */
    void supprimerImage(String imageLink);

    /**
     * Vérifie si deux images sont identiques (par hash)
     * 
     * @param base64Image1 Première image en base64
     * @param base64Image2 Deuxième image en base64
     * @return true si les images sont identiques
     */
    boolean sontImagesIdentiques(String base64Image1, String base64Image2);
}
