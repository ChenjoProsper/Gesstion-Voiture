package com.gestion_voiture.gestionnaire.mapper;

import org.springframework.stereotype.Component;

import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.pattern.abstractfactory.FabriqueVehiculeElectrique;
import com.gestion_voiture.gestionnaire.pattern.abstractfactory.FabriqueVehiculeEssence;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehiculeMapper {

    private final FabriqueVehiculeElectrique fabriqueVehiculeElectrique;
    private final FabriqueVehiculeEssence fabriqueVehiculeEssence;

    public VehiculeResultDTO toDto(Vehicule vehicule) {
        if (vehicule == null)
            return null;
        VehiculeResultDTO vehiculeResultDTO = new VehiculeResultDTO();
        vehiculeResultDTO.setId(vehicule.getId());
        vehiculeResultDTO.setReference(vehicule.getReference());
        vehiculeResultDTO.setMarque(vehicule.getMarque());
        vehiculeResultDTO.setModele(vehicule.getModele());
        vehiculeResultDTO.setDescription(vehicule.getDescription());
        vehiculeResultDTO.setPrixBase(vehicule.getPrixBase());
        vehiculeResultDTO.setImageLink(vehicule.getImageLink());

        return vehiculeResultDTO;
    }

    // Mapping du DTO vers l'entité (Logique de création)
    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null)
            return null;

        Vehicule vehicule = switch (dto.getTypeVehicule()) {

            case AUTO_ELECTRIQUE -> fabriqueVehiculeElectrique.creerAutomobile();
            case AUTO_ESSENCE -> fabriqueVehiculeEssence.creerAutomobile();
            case SCOOTER_ELECTRIQUE -> fabriqueVehiculeElectrique.creerScooter();
            case SCOOTER_ESSENCE -> fabriqueVehiculeEssence.creerScooter();
        };

        vehicule.setReference(dto.getReference());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setPrixBase(dto.getPrixBase());
        return vehicule;
    }
}