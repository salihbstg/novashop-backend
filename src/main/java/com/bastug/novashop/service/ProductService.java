package com.bastug.novashop.service;

import com.bastug.novashop.dto.ProductResponse;
import com.bastug.novashop.dto.ProductSaveRequest;
import com.bastug.novashop.dto.ProductUpdateRequest;
import com.bastug.novashop.mapper.ProductMapper;
import com.bastug.novashop.product.Product;
import com.bastug.novashop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    //Ürün oluşturma
    public ProductResponse createProduct(ProductSaveRequest productSaveRequest) {
        Product product = productRepository.save(productMapper.toEntity(productSaveRequest));
        return productMapper.toProductResponse(product);
    }

    //Ürün güncelleme
    public ProductResponse updateProduct(ProductUpdateRequest productUpdateRequest) {
        Optional<Product> optionalProduct=productRepository.findById(productUpdateRequest.id());
        if (optionalProduct.isPresent()) {
            Product product=productMapper.updateProductFromRequest(productUpdateRequest,optionalProduct.get());
            return productMapper.toProductResponse(productRepository.save(product));
        }
        return null;
    }

    //Tüm ürünleri listele
    public List<ProductResponse> getAllProducts() {
        List<Product> products=productRepository.findAll();
        List<ProductResponse> productResponses=new ArrayList<>();
        for (Product product:products) {
            productResponses.add(productMapper.toProductResponse(product));
        }
        return productResponses;
    }

    //id ile ürün listele
    public ProductResponse getProductById(Long id) {
        Optional<Product> optionalProduct=productRepository.findById(id);
        return optionalProduct.map(productMapper::toProductResponse).orElse(null);
    }

    //id ile ürün silme
    public String deleteProduct(Long id) {
        productRepository.deleteById(id);
        return "ID'li ürün silindi!";
    }

}
