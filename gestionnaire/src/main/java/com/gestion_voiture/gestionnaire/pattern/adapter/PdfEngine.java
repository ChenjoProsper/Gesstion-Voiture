package com.gestion_voiture.gestionnaire.pattern.adapter;

import org.springframework.stereotype.Component;

@Component
public class PdfEngine {
    public String renderPdfData(String text) {
        return "[PDF Rendering] : << " + text + " >> (Format PDF optimisé)";
    }
}