package com.gmp.edms.repository;

import com.gmp.edms.entity.ElasticsearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticsearchDocumentRepository extends ElasticsearchRepository<ElasticsearchDocument, Long> {
}
