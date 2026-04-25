package com.bastug.novashop.service;

import com.bastug.novashop.dto.ProductResponse;
import com.bastug.novashop.dto.ProductSaveRequest;
import com.bastug.novashop.mapper.ProductMapper;
import com.bastug.novashop.product.Product;
import com.bastug.novashop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductResponse createProduct(ProductSaveRequest productSaveRequest) {

        Product product = productRepository.save(productMapper.toEntity(productSaveRequest));
        return productMapper.toProductResponse(product);
    }
}
