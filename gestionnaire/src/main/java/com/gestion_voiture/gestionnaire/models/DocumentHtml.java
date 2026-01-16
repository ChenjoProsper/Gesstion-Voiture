package com.gestion_voiture.gestionnaire.models;

import com.gestion_voiture.gestionnaire.pattern.adapter.HtmlEngine;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentHtml extends Document {

    @Transient
    private final HtmlEngine htmlEngine = new HtmlEngine();

    public DocumentHtml() {
        super();
    }

    @Override
    public String genereContenu() {
        return htmlEngine.renderHtmlCode(this.getContenu());
    }
}