package com.gestion_voiture.gestionnaire.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.gestion_voiture.gestionnaire.models.Option;
import com.gestion_voiture.gestionnaire.models.Panier;
import com.gestion_voiture.gestionnaire.models.PanierItem;
import com.gestion_voiture.gestionnaire.models.Vehicule;
import com.gestion_voiture.gestionnaire.repository.OptionRepository;
import com.gestion_voiture.gestionnaire.repository.PanierRepository;
import com.gestion_voiture.gestionnaire.repository.VehiculeRepository;
import com.gestion_voiture.gestionnaire.services.PanierService;

@Service
@Transactional
public class PanierServiceImpl implements PanierService {

    private final PanierRepository panierRepository;
    private final VehiculeRepository vehiculeRepository;
    private final OptionRepository optionRepository;

    // In-memory mementos for simple undo per client (non-persistent)
    private final Map<Long, Panier> snapshots = new HashMap<>();

    public PanierServiceImpl(PanierRepository panierRepository, VehiculeRepository vehiculeRepository, OptionRepository optionRepository) {
        this.panierRepository = panierRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public Panier getPanierByClient(Long clientId) {
        return panierRepository.findByClientId(clientId).orElseGet(() -> {
            Panier p = new Panier();
            p.setClientId(clientId);
            return panierRepository.save(p);
        });
    }

    @Override
    public Panier addItem(Long clientId, Long vehiculeId, List<Long> optionIds, Integer quantite) {
        Panier panier = getPanierByClient(clientId);
        // Snapshot for undo
        snapshots.put(clientId, clonePanier(panier));

        Optional<Vehicule> vOpt = vehiculeRepository.findById(vehiculeId);
        if (!vOpt.isPresent()) {
            throw new IllegalArgumentException("Véhicule introuvable: " + vehiculeId);
        }

        List<Option> options = new ArrayList<>();
        if (optionIds != null) {
            optionRepository.findAllById(optionIds).forEach(options::add);
        }

        // Vérifier la compatibilité entre options sélectionnées pour cet item
        for (int i = 0; i < options.size(); i++) {
            for (int j = i + 1; j < options.size(); j++) {
                Option a = options.get(i);
                Option b = options.get(j);
                if (!a.estCompatible(b) || !b.estCompatible(a)) {
                    throw new IllegalArgumentException("Options incompatibles sélectionnées: " + a.getNom() + " et " + b.getNom());
                }
            }
        }

        PanierItem item = new PanierItem();
        item.setVehicule(vOpt.get());
        item.setOptions(options);
        item.setQuantite(quantite != null ? quantite : 1);

        panier.getItems().add(item);
        panier.touch();

        return panierRepository.save(panier);
    }

    @Override
    public Panier removeItem(Long clientId, Long panierItemId) {
        Panier panier = getPanierByClient(clientId);
        snapshots.put(clientId, clonePanier(panier));

        panier.getItems().removeIf(i -> i.getId() != null && i.getId().equals(panierItemId));
        panier.touch();
        return panierRepository.save(panier);
    }

    @Override
    public Panier clearCart(Long clientId) {
        Panier panier = getPanierByClient(clientId);
        snapshots.put(clientId, clonePanier(panier));
        panier.getItems().clear();
        panier.touch();
        return panierRepository.save(panier);
    }

    @Override
    public boolean undo(Long clientId) {
        Panier snapshot = snapshots.get(clientId);
        if (snapshot == null) return false;
        // Overwrite current panier with snapshot
        Optional<Panier> current = panierRepository.findByClientId(clientId);
        if (current.isPresent()) {
            Panier p = current.get();
            p.getItems().clear();
            p.getItems().addAll(snapshot.getItems());
            p.touch();
            panierRepository.save(p);
        } else {
            panierRepository.save(snapshot);
        }
        snapshots.remove(clientId);
        return true;
    }

    private Panier clonePanier(Panier src) {
        Panier copy = new Panier();
        copy.setClientId(src.getClientId());
        for (PanierItem i : src.getItems()) {
            PanierItem ci = new PanierItem();
            ci.setVehicule(i.getVehicule());
            ci.setOptions(new ArrayList<>(i.getOptions() != null ? i.getOptions() : new ArrayList<>()));
            ci.setQuantite(i.getQuantite());
            copy.getItems().add(ci);
        }
        return copy;
    }
}
