package com.gestion_voiture.gestionnaire.mapper;
import com.gestion_voiture.gestionnaire.dto.VehiculeResultDTO;
import com.gestion_voiture.gestionnaire.dto.VehiculeDTO;
import com.gestion_voiture.gestionnaire.models.*;
import org.springframework.stereotype.Component;

@Component
public class VehiculeMapper {

    // Mapping de l'entité vers le ResultDTO

    public VehiculeResultDTO toDto(Vehicule vehicule){

        VehiculeResultDTO vehiculeResultDTO = new VehiculeResultDTO();
        vehiculeResultDTO.setId(vehicule.getId());
        vehiculeResultDTO.setReference(vehicule.getReference());
        vehiculeResultDTO.setMarque(vehicule.getMarque());
        vehiculeResultDTO.setModele(vehicule.getModele());
        vehiculeResultDTO.setDescription(vehicule.getDescription());
        vehiculeResultDTO.setPrixBase(vehicule.getPrixBase());

        return vehiculeResultDTO;
    }

    // Mapping du DTO vers l'entité (Logique de création)
    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null) return null;

        Vehicule vehicule = switch (dto.getTypeVehicule()) {

            case AUTO_ELECTRIQUE      -> new AutoElectrique();
            case AUTO_ESSENCE         -> new AutoEssence();
            case SCOOTER_ELECTRIQUE   -> new ScooterElectrique();
            case SCOOTER_ESSENCE      -> new ScooterEssence();
        };

        vehicule.setReference(dto.getReference());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setPrixBase(dto.getPrixBase());
        return vehicule;
    }
}