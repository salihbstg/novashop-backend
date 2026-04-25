package com.bastug.novashop.product.controller;

import com.bastug.novashop.product.dto.ProductResponse;
import com.bastug.novashop.product.dto.ProductSaveRequest;
import com.bastug.novashop.product.dto.ProductUpdateRequest;
import com.bastug.novashop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProducts(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductSaveRequest productSaveRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productSaveRequest));
    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(productUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(id + productService.deleteProduct(id));
    }
}
