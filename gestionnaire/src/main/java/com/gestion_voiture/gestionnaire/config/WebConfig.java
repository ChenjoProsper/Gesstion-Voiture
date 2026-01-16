package com.gestion_voiture.gestionnaire.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapper l'URL /uploads/** vers le dossier physique uploads
        exposeDirectory("uploads", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Construire le chemin avec le préfixe file:// (3 slashs au total pour Linux)
        // Sur Linux: file:///home/... au lieu de file:/home/...
        String fileLocation = "file://" + uploadPath + "/";

        System.out.println("=== WebConfig ===");
        System.out.println("Chemin absolu du dossier uploads: " + uploadPath);
        System.out.println("Ressource mappée: " + fileLocation);
        System.out.println("URL accessible: http://localhost:8080/" + dirName + "/**");
        System.out.println("================");

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations(fileLocation)
                .setCachePeriod(3600);
    }
}
