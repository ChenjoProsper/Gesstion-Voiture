package com.gestion_voiture.gestionnaire.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;

@Service
public interface VehiculeService {
    VehiculeResultDTO ajouterVehicule(VehiculeDTO dto);
    List<VehiculeResultDTO> listerCatalogue();
    VehiculeResultDTO ajouterOption(Long vehiculeId, Long optionId);
    public Double calculerPrixReel(Long vehiculeId, List<Long> optionsIds);
}