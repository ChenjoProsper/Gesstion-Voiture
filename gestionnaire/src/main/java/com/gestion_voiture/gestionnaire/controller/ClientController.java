package com.gestion_voiture.gestionnaire.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/client")
@Tag(name="clients", description = "gestion des clients")
@RequiredArgsConstructor
public class ClientController {
    
}
