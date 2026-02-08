package com.gestion_voiture.gestionnaire.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "panier_items")
public class PanierItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    private Integer quantite = 1;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "panier_item_options",
        joinColumns = @JoinColumn(name = "panier_item_id"),
        inverseJoinColumns = @JoinColumn(name = "option_id"))
    private List<Option> options;

    public PanierItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }
}
