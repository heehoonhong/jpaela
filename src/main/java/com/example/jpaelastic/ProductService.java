package com.example.jpaelastic;


import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.jpaelastic.domain.CreateProductRequestDto;
import com.example.jpaelastic.domain.Product;
import com.example.jpaelastic.domain.ProductDocument;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;

import org.springframework.stereotype.Service;




import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDocumentRepository productDocumentRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public Product createProduct(CreateProductRequestDto dto) {
        // 1. JPA 저장
        Product product = new Product(null, dto.getName(), dto.getCategory());
        Product savedProduct = productRepository.save(product);

        // 2. Elasticsearch 저장 (ID는 문자열이어야 하므로 Long -> String 변환)
        ProductDocument document = new ProductDocument(
                savedProduct.getId().toString(),
                savedProduct.getName(),
                savedProduct.getCategory()
        );
        productDocumentRepository.save(document);

        return savedProduct;
    }

    // JPA 검색
    public List<Product> searchByNameWithJpa(String name) {
        return productRepository.findByNameContaining(name);
    }

    public List<ProductDocument> searchByNameWithElasticsearch(String name) {
        // match 쿼리 생성 (ES Java Client 방식)
        Query matchQuery = MatchQuery.of(m -> m
                .field("name")
                .query(name)
        )._toQuery();

        // NativeQuery 빌드
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(matchQuery)
                .withPageable(PageRequest.of(0, 10)) // 페이징 적용 (원하면 파라미터로 받기)
                .build();

        // Elasticsearch 검색 실행
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(nativeQuery, ProductDocument.class);

        // 결과 추출
        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .toList();
    }

    public void syncProductsToElasticsearch() {
        List<Product> products = productRepository.findAll();

        List<ProductDocument> documents = products.stream()
                .map(p -> new ProductDocument(
                        p.getId().toString(),
                        p.getName(),
                        p.getCategory()
                ))
                .toList();

        int chunkSize = 1000;
        for (int i = 0; i < documents.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, documents.size());
            List<ProductDocument> chunk = documents.subList(i, end);
            productDocumentRepository.saveAll(chunk); // 👈 1000개씩 저장
        }

        System.out.println("✅ Elasticsearch에 총 " + documents.size() + "개 저장 완료");
    }

}
