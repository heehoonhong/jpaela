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

    @GetMapping("/search/es")
    public ResponseEntity<List<ProductDocument>> searchWithElasticsearch(@RequestParam String name) {
        List<ProductDocument> products = productService.searchByNameWithElasticsearch(name);
        return ResponseEntity.ok(products);
    }
}
