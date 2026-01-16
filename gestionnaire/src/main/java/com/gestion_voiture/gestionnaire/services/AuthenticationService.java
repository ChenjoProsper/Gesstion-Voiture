package com.gestion_voiture.gestionnaire.services;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.AuthRequestDTO;
import com.gestion_voiture.gestionnaire.dto.AuthResponseDTO;
import com.gestion_voiture.gestionnaire.dto.RegisterRequestDTO;

@Service
public interface AuthenticationService {
    AuthResponseDTO login(AuthRequestDTO request);

    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO logout(Long clientId);
}
