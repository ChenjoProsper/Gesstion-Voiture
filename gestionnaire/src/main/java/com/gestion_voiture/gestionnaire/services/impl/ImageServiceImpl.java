package com.gestion_voiture.gestionnaire.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.services.ImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${app.upload.dir:uploads/vehicules}")
    private String uploadDir;

    @Override
    public String sauvegarderImage(String base64Image, Long vehiculeId) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        // Décoder le base64
        String base64Data = base64Image;
        if (base64Data.contains(",")) {
            base64Data = base64Data.split(",")[1]; // Enlever le préfixe "data:image/jpeg;base64,"
        }

        byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Data);

        // Créer le répertoire s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        // Générer un nom de fichier unique basé sur l'ID du véhicule
        String fileName = "vehicule-" + vehiculeId + ".jpg";
        Path filePath = uploadPath.resolve(fileName);

        // Sauvegarder le fichier
        Files.write(filePath, decodedBytes);

        log.info("Image sauvegardée: {}", filePath.toAbsolutePath());

        // Retourner le lien relatif
        return "/uploads/vehicules/" + fileName;
    }

    @Override
    public void supprimerImage(String imageLink) {
        if (imageLink == null || imageLink.isEmpty()) {
            return;
        }

        try {
            // Convertir le lien relatif en chemin absolu
            String fileName = imageLink.replace("/uploads/vehicules/", "");
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Image supprimée: {}", filePath.toAbsolutePath());
            } else {
                log.warn("Fichier image non trouvé: {}", filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Erreur lors de la suppression de l'image: {}", imageLink, e);
        }
    }

    @Override
    public boolean sontImagesIdentiques(String base64Image1, String base64Image2) {
        if (base64Image1 == null || base64Image2 == null) {
            return false;
        }

        try {
            String hash1 = calculerHashImage(base64Image1);
            String hash2 = calculerHashImage(base64Image2);
            return hash1.equals(hash2);
        } catch (NoSuchAlgorithmException e) {
            log.error("Erreur lors du calcul du hash des images", e);
            return false;
        }
    }

    private String calculerHashImage(String base64Image) throws NoSuchAlgorithmException {
        String base64Data = base64Image;
        if (base64Data.contains(",")) {
            base64Data = base64Data.split(",")[1];
        }

        byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Data);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(decodedBytes);

        // Convertir en hexadécimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
