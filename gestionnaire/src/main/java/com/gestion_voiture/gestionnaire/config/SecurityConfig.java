package com.gestion_voiture.gestionnaire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    // Injection de la configuration CORS que tu as créée dans CorsConfig.java
    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    /**
     * Bean pour encoder les mots de passe avec BCrypt.
     * Utilisé lors du Register pour hacher le mot de passe avant insertion en BD.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration principale de la sécurité Spring.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Liaison avec ton fichier CorsConfig.java
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // 2. Désactivation du CSRF (nécessaire pour les APIs REST Stateless / POST)
                .csrf(csrf -> csrf.disable())

                // 3. Politique de session sans état (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Gestion des autorisations par route
                .authorizeHttpRequests(authz -> authz
                        // --- Toutes les routes sont publiques ---
                        .anyRequest().permitAll());

        return http.build();
    }
}