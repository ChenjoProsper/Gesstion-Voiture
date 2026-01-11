package com.gestion_voiture.gestionnaire.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.mapper.VehiculeMapper;
import com.gestion_voiture.gestionnaire.models.AutoElectrique;
import com.gestion_voiture.gestionnaire.models.AutoEssence;
import com.gestion_voiture.gestionnaire.models.ScooterElectrique;
import com.gestion_voiture.gestionnaire.models.ScooterEssence;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.services.VehiculeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public List<VehiculeResultDTO> listerCatalogue() {

        List<Vehicule> stock = vehiculeRepository.findAll();
        Iterator<Vehicule> it = stock.iterator();
        
        List<VehiculeResultDTO> results = new ArrayList<>();
        while(it.hasNext()) {
            results.add(vehiculeMapper.toDto(it.next()));
        }
        return results;
    }

    @Override
    public VehiculeResultDTO ajouterVehicule(VehiculeDTO dto) {
        // Logique de Factory simple selon le type passé dans le DTO
        
        Vehicule v = switch (dto.getTypeVehicule()) {

            case AUTO_ELECTRIQUE      -> new AutoElectrique();
            case AUTO_ESSENCE         -> new AutoEssence();
            case SCOOTER_ELECTRIQUE   -> new ScooterElectrique();
            case SCOOTER_ESSENCE      -> new ScooterEssence();
        };
        
        v.setMarque(dto.getMarque());
        v.setModele(dto.getModele());
        v.setPrixBase(dto.getPrixBase());
        
        return vehiculeMapper.toDto(vehiculeRepository.save(v));
    }
}