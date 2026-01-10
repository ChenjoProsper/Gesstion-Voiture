package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public abstract class Scooter extends Vehicule {
    private Integer cylindree;
}