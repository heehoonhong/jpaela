package com.example.jpaelastic;


import com.example.jpaelastic.domain.CreateProductRequestDto;
import com.example.jpaelastic.domain.Product;
import com.example.jpaelastic.domain.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto) {
        Product product = productService.createProduct(createProductRequestDto);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/search/jpa")
    public ResponseEntity<List<Product>> searchWithJpa(@RequestParam String name) {
        List<Product> products = productService.searchByNameWithJpa(name);
        return ResponseEntity.ok(products);
    }
    /*
    @GetMapping("/search/es")
    public ResponseEntity<List<ProductDocument>> searchWithElasticsearch(@RequestParam String name) {
        List<ProductDocument> products = productService.searchByNameWithElasticsearch(name);
        return ResponseEntity.ok(products);
    }
    */


    // 1) 동기 방식으로 “알림”까지 모두 처리하고 응답
    @PostMapping("/test/sync")
    public ResponseEntity<Product> createSync(@RequestBody CreateProductRequestDto dto) {
        long start = System.currentTimeMillis();

        // JPA 저장 + 알림(동기)
        Product saved = productService.createProduct(dto);
        productService.notifyProductCreatedSync(saved);  // 아래에서 구현할 동기 알림

        long duration = System.currentTimeMillis() - start;
        System.out.println("⏱ [동기] 전체 소요: " + duration + "ms");
        return ResponseEntity
                .ok()
                .header("X-Duration-ms", String.valueOf(duration))
                .body(saved);
    }

    // 2) 비동기 방식으로 알림 분리하고 바로 응답
    @PostMapping("/test/async")
    public ResponseEntity<Product> createAsync(@RequestBody CreateProductRequestDto dto) {
        long start = System.currentTimeMillis();

        // JPA 저장 + 알림(비동기)
        Product saved = productService.createProduct(dto);
        productService.notifyProductCreatedAsync(saved);  // @Async 메서드

        long duration = System.currentTimeMillis() - start;
        System.out.println("⏱ [비동기] 응답까지 소요: " + duration + "ms");
        return ResponseEntity
                .ok()
                .header("X-Duration-ms", String.valueOf(duration))
                .body(saved);
    }
}
