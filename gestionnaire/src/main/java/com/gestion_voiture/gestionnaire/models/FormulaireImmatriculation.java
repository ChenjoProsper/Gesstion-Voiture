package com.gestion_voiture.gestionnaire.models;

public class FormulaireImmatriculation extends Formulaire {
    public FormulaireImmatriculation(FormulaireImpl impl) {
        super(impl);
    }

    @Override
    public void afficher() {
        impl.dessinerTexte("DEMANDE D'IMMATRICULATION");
        impl.dessinerZoneSaisie("Numéro de châssis");
        impl.dessinerZoneSaisie("Nom du propriétaire");
    }
}