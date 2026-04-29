package com.bastug.novashop.product;

import com.bastug.novashop.product.dto.ProductResponse;
import com.bastug.novashop.product.dto.ProductSaveRequest;
import com.bastug.novashop.product.entity.Product;
import com.bastug.novashop.product.mapper.ProductMapper;
import com.bastug.novashop.product.repository.ProductRepository;
import com.bastug.novashop.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private final ProductMapper productMapper= Mappers.getMapper(ProductMapper.class);
    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldReturnProductResponse_whenRequestIsValid() {

        ProductSaveRequest productSaveRequest = new ProductSaveRequest(
                "product-name",
                "description",
                new BigDecimal(1000),
                "url_url_",
                100

        );

        Product product = new Product();
        product.setProductName("product-name");
        product.setProductDescription("description");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setProductPrice(BigDecimal.valueOf(1000));
        product.setImageUrl("url_url_");
        product.setId(1L);
        when(productMapper.toEntity(productSaveRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        ProductResponse productResponse = productService.createProduct(productSaveRequest);
        assertThat(productResponse.productName()).isEqualTo("product-name");
        assertThat(productResponse.productDescription()).isEqualTo("description");
        assertThat(productResponse.productPrice()).isEqualTo(new BigDecimal(1000));
        assertThat(productResponse.imageUrl()).isEqualTo("url_url_");
        assertThat(productResponse.id()).isEqualTo(1L);
        assertThat(productResponse.stock()).isEqualTo(100);
    }

    @Test
    void updateProduct_shouldReturnProductResponse_whenRequestIsValid() {
        ProductSaveRequest productSaveRequest = new ProductSaveRequest(
                "updated name",
                "updated desc",
                new BigDecimal(1000),
                "updated url",
                100
        );

        Product product = new Product();
        product.setProductName("old name");
        product.setProductDescription("old description");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setProductPrice(BigDecimal.valueOf(1000));
        product.setImageUrl("old url");
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductResponse productResponse = productService.updateProduct(productSaveRequest,1L);
        assertThat(productResponse.productName()).isEqualTo("updated name");
        assertThat(productResponse.productDescription()).isEqualTo("updated desc");
        assertThat(productResponse.imageUrl()).isEqualTo("updated url");
        assertThat(productResponse.productPrice()).isEqualTo(new BigDecimal(1000));

    }
}
