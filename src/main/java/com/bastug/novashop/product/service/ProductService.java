package com.bastug.novashop.product.service;

import com.bastug.novashop.exception.ApplicationExceptionImpl;
import com.bastug.novashop.product.dto.ProductResponse;
import com.bastug.novashop.product.dto.ProductSaveRequest;
import com.bastug.novashop.product.mapper.ProductMapper;
import com.bastug.novashop.product.entity.Product;
import com.bastug.novashop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public ProductResponse updateProduct(ProductSaveRequest productSaveRequest, Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = productMapper.updateProductFromRequest(productSaveRequest, optionalProduct.get());
            return productMapper.toProductResponse(productRepository.save(product));
        }
        throw new ApplicationExceptionImpl("Ürün bulunamadı");
    }

    //Tüm ürünleri listele
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toProductResponse);
    }

    //id ile ürün listele
    public ProductResponse getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(productMapper::toProductResponse).orElseThrow(() -> new ApplicationExceptionImpl("Product not found"));
    }

    //id ile ürün silme
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            throw new ApplicationExceptionImpl("Ürün bulunamadı!");
        }
        productRepository.deleteById(id);
    }

}
