package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentQueryDTO;
import com.gmp.edms.entity.ElasticsearchDocument;
import com.gmp.edms.repository.ElasticsearchDocumentRepository;
import com.gmp.edms.service.DocumentSearchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentSearchServiceImpl implements DocumentSearchService {

    private final ElasticsearchDocumentRepository elasticsearchDocumentRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ModelMapper modelMapper;

    @Override
    public List<DocumentDTO> fullTextSearch(String query) {
        Criteria criteria = new Criteria("documentName").contains(query)
                .or(new Criteria("documentCode").contains(query))
                .or(new Criteria("description").contains(query))
                .or(new Criteria("content").contains(query));

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        SearchHits<ElasticsearchDocument> searchHits = elasticsearchOperations.search(criteriaQuery,
                ElasticsearchDocument.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(elasticsearchDocument -> modelMapper.map(elasticsearchDocument, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentDTO> advancedSearch(DocumentQueryDTO queryDTO) {
        Criteria criteria = new Criteria();

        if (queryDTO.getDocumentName() != null && !queryDTO.getDocumentName().isEmpty()) {
            criteria = criteria.and(new Criteria("documentName").contains(queryDTO.getDocumentName()));
        }

        if (queryDTO.getDocumentCode() != null && !queryDTO.getDocumentCode().isEmpty()) {
            criteria = criteria.and(new Criteria("documentCode").contains(queryDTO.getDocumentCode()));
        }

        if (queryDTO.getDocumentType() != null && !queryDTO.getDocumentType().isEmpty()) {
            criteria = criteria.and(new Criteria("documentType").is(queryDTO.getDocumentType()));
        }

        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            criteria = criteria.and(new Criteria("status").is(queryDTO.getStatus()));
        }

        if (queryDTO.getConfidentialityLevel() != null && !queryDTO.getConfidentialityLevel().isEmpty()) {
            criteria = criteria.and(new Criteria("confidentialityLevel").is(queryDTO.getConfidentialityLevel()));
        }

        if (queryDTO.getAuthor() != null && !queryDTO.getAuthor().isEmpty()) {
            criteria = criteria.and(new Criteria("author").contains(queryDTO.getAuthor()));
        }

        if (queryDTO.getCategoryId() != null) {
            criteria = criteria.and(new Criteria("categoryId").is(queryDTO.getCategoryId()));
        }

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        SearchHits<ElasticsearchDocument> searchHits = elasticsearchOperations.search(criteriaQuery,
                ElasticsearchDocument.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(elasticsearchDocument -> modelMapper.map(elasticsearchDocument, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void indexDocument(DocumentDTO documentDTO) {
        ElasticsearchDocument elasticsearchDocument = modelMapper.map(documentDTO, ElasticsearchDocument.class);
        elasticsearchDocumentRepository.save(elasticsearchDocument);
    }

    @Override
    public void updateDocumentIndex(DocumentDTO documentDTO) {
        ElasticsearchDocument elasticsearchDocument = modelMapper.map(documentDTO, ElasticsearchDocument.class);
        elasticsearchDocumentRepository.save(elasticsearchDocument);
    }

    @Override
    public void deleteDocumentIndex(Long documentId) {
        elasticsearchDocumentRepository.deleteById(documentId);
    }

    @Override
    public void bulkIndexDocuments(List<DocumentDTO> documentDTOs) {
        List<ElasticsearchDocument> elasticsearchDocuments = documentDTOs.stream()
                .map(documentDTO -> modelMapper.map(documentDTO, ElasticsearchDocument.class))
                .collect(Collectors.toList());
        elasticsearchDocumentRepository.saveAll(elasticsearchDocuments);
    }
}
