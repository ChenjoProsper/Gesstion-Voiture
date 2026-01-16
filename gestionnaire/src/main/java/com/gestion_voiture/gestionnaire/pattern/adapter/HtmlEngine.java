package com.gestion_voiture.gestionnaire.pattern.adapter;

import org.springframework.stereotype.Component;

@Component
public class HtmlEngine {
    public String renderHtmlCode(String content) {
        return "<html><body><div class='content'>" + content + "</div></body></html>";
    }
}