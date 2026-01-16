package com.gestion_voiture.gestionnaire.pattern.command;

public interface Command {
    void execute();

    void undo();
}