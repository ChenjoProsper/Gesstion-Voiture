package com.gestion_voiture.gestionnaire.controller;

import com.gestion_voiture.gestionnaire.models.Option;
import com.gestion_voiture.gestionnaire.repository.OptionRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionController {

    private final OptionRepository optionRepository;

    @PostMapping
    @Operation(summary = "creer une option")
    public Option creerOption(@RequestBody Option option) {
        return optionRepository.save(option);
    }

    @GetMapping
    @Operation(summary = "lister toutes les options")
    public List<Option> listerOptions() {
        return optionRepository.findAll();
    }
}