package com.gestion_voiture.gestionnaire.pattern.composite;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import com.gestion_voiture.gestionnaire.models.Client;

/**
 * Feuille du Composite : Représente un client simple (sans filiales)
 */
@Data
public class ClientSimple extends Client implements ClientComposite {
    
    @Override
    public void addClient(ClientComposite client) {
        throw new UnsupportedOperationException("Un client simple ne peut pas avoir de filiales");
    }
    
    @Override
    public void removeClient(ClientComposite client) {
        throw new UnsupportedOperationException("Un client simple ne peut pas avoir de filiales");
    }
    
    @Override
    public List<ClientComposite> getClients() {
        return new ArrayList<>();
    }
    
    @Override
    public void afficher(int niveau) {
        String indent = "  ".repeat(niveau);
        System.out.println(indent + "├─ [CLIENT] " + this.getNom());
    }
    
    @Override
    public int compterClients() {
        return 1;
    }
}
