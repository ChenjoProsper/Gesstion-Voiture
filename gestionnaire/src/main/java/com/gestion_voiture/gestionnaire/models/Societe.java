package com.gestion_voiture.gestionnaire.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import com.gestion_voiture.gestionnaire.pattern.composite.ClientComposite;
import lombok.Data;

@Entity
@Data
public class Societe extends Client implements ClientComposite {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    private List<Client> filiales = new ArrayList<>();
    
    private String numeroSIREN;
    private String adresseSiege;

    @Override
    public void ajouteFiliale(Client filiale) {
        this.filiales.add(filiale);
    }

    @Override
    public void addClient(ClientComposite client) {
        if (client instanceof Client) {
            filiales.add((Client) client);
        }
    }
    
    @Override
    public void removeClient(ClientComposite client) {
        if (client instanceof Client) {
            filiales.remove((Client) client);
        }
    }
    
    @Override
    public List<ClientComposite> getClients() {
        List<ClientComposite> result = new ArrayList<>();
        for (Client client : filiales) {
            if (client instanceof ClientComposite) {
                result.add((ClientComposite) client);
            }
        }
        return result;
    }
    
    @Override
    public void afficher(int niveau) {
        String indent = "  ".repeat(niveau);
        System.out.println(indent + "┌─ [SOCIÉTÉ] " + this.getNom() + " (SIREN: " + numeroSIREN + ")");
        
        for (Client filiale : filiales) {
            if (filiale instanceof ClientComposite) {
                ((ClientComposite) filiale).afficher(niveau + 1);
            }
        }
        
        System.out.println(indent + "└─ Total: " + compterClients() + " client(s)");
    }
    
    public int compterClients() {
        int count = filiales.size();
        for (Client filiale : filiales) {
            if (filiale instanceof Societe) {
                count += ((Societe) filiale).compterClients();
            }
        }
        return count;
    }
}
