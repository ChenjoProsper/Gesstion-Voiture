package com.gestion_voiture.gestionnaire.services.impl;

import org.springframework.stereotype.Service;

import com.gestion_voiture.gestionnaire.dto.DocumentHtmlDTO;
import com.gestion_voiture.gestionnaire.dto.DocumentPdfDTO;
import com.gestion_voiture.gestionnaire.mapper.DocumentMapper;
import com.gestion_voiture.gestionnaire.models.Document;
import com.gestion_voiture.gestionnaire.models.DocumentHtml;
import com.gestion_voiture.gestionnaire.repository.DocumentRepository;
import com.gestion_voiture.gestionnaire.services.DocumentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Override
    public Document creerDocumentPdf(DocumentPdfDTO dto) {
        Document pdf = documentMapper.toEntity(dto);
        return documentRepository.save(pdf);
    }

    @Override
    public Document creerDocumentHtml(DocumentHtmlDTO dto) {
        DocumentHtml html = documentMapper.toEntity(dto);
        return documentRepository.save(html);
    }
}