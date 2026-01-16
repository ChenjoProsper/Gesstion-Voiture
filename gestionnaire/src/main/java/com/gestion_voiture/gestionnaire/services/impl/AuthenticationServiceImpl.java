package com.gestion_voiture.gestionnaire.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion_voiture.gestionnaire.dto.AuthRequestDTO;
import com.gestion_voiture.gestionnaire.dto.AuthResponseDTO;
import com.gestion_voiture.gestionnaire.dto.RegisterRequestDTO;
import com.gestion_voiture.gestionnaire.models.Client;
import com.gestion_voiture.gestionnaire.models.ClientParticulier;
import com.gestion_voiture.gestionnaire.models.Societe;
import com.gestion_voiture.gestionnaire.repository.ClientRepository;
import com.gestion_voiture.gestionnaire.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponseDTO login(AuthRequestDTO request) {
        // Vérifier que l'email et le mot de passe sont fournis
        if (request.getEmail() == null || request.getPassword() == null) {
            return new AuthResponseDTO(null, null, null, false, "Email et mot de passe requis");
        }

        // Chercher le client par email
        Client client = clientRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (client == null) {
            return new AuthResponseDTO(null, null, null, false, "Client non trouvé");
        }

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
            return new AuthResponseDTO(null, null, null, false, "Mot de passe incorrect");
        }

        // Marquer le client comme authentifié
        client.setAuthenticated(true);
        clientRepository.save(client);

        return new AuthResponseDTO(
                client.getId(),
                client.getNom(),
                client.getEmail(),
                true,
                "Authentification réussie");
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Vérifier que tous les champs requis sont présents
        if (request.getEmail() == null || request.getPassword() == null ||
                request.getNom() == null || request.getPassword().isBlank()) {
            return new AuthResponseDTO(null, null, null, false, "Tous les champs requis doivent être remplis");
        }

        // Vérifier que l'email n'existe pas déjà
        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponseDTO(null, null, null, false, "Cet email est déjà utilisé");
        }

        // Vérifier que les mots de passe correspondent
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new AuthResponseDTO(null, null, null, false, "Les mots de passe ne correspondent pas");
        }

        // Créer le client approprié
        Client client;
        if (request.isSociete()) {
            Societe societe = new Societe();
            societe.setNom(request.getNom());
            societe.setEmail(request.getEmail());
            societe.setTelephone(request.getTelephone());
            societe.setPassword(passwordEncoder.encode(request.getPassword()));
            societe.setAuthenticated(true);

            // Gérer la filiale si parentId est fourni
            if (request.getParentId() != null) {
                Client parent = clientRepository.findById(request.getParentId())
                        .orElse(null);
                if (parent != null && parent instanceof Societe) {
                    ((Societe) parent).ajouteFiliale(societe);
                }
            }
            client = societe;
        } else {
            ClientParticulier particulier = new ClientParticulier();
            particulier.setNom(request.getNom());
            particulier.setEmail(request.getEmail());
            particulier.setTelephone(request.getTelephone());
            particulier.setPassword(passwordEncoder.encode(request.getPassword()));
            particulier.setAuthenticated(true);
            client = particulier;
        }

        Client savedClient = clientRepository.save(client);

        return new AuthResponseDTO(
                savedClient.getId(),
                savedClient.getNom(),
                savedClient.getEmail(),
                true,
                "Inscription réussie");
    }

    @Override
    @Transactional
    public AuthResponseDTO logout(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElse(null);

        if (client == null) {
            return new AuthResponseDTO(null, null, null, false, "Client non trouvé");
        }

        client.setAuthenticated(false);
        clientRepository.save(client);

        return new AuthResponseDTO(
                client.getId(),
                client.getNom(),
                client.getEmail(),
                false,
                "Déconnexion réussie");
    }
}
