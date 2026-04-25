package com.bastug.novashop.product.controller;

import com.bastug.novashop.product.dto.ProductResponse;
import com.bastug.novashop.product.dto.ProductSaveRequest;
import com.bastug.novashop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //ID'ye göre ürün listeleme
    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    //Tüm ürünleri listeleme
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    //Ürün kayıt
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductSaveRequest productSaveRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productSaveRequest));
    }

    //Ürün güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody ProductSaveRequest productSaveRequest,@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(productSaveRequest,id));
    }

    //Ürün silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
