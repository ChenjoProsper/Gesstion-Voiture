package com.gestion_voiture.gestionnaire.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "paniers")
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PanierItem> items = new ArrayList<>();

    private Instant updatedAt = Instant.now();

    public Panier() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public List<PanierItem> getItems() { return items; }
    public void setItems(List<PanierItem> items) { this.items = items; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public void touch() {
        this.updatedAt = Instant.now();
    }
}
