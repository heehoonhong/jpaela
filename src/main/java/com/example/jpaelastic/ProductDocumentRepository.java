package com.example.jpaelastic;

import com.example.jpaelastic.domain.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByName(String name); // 정확히 일치
    List<ProductDocument> findByNameContaining(String name); // 일부 포함 (자동 생성 가능성 낮음)

}
