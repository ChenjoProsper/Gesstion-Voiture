package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public abstract class Automobile extends Vehicule {
    private Integer nombrePortes;
    private Double espaceCoffre;
}