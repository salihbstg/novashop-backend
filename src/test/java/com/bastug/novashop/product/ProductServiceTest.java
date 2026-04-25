package com.bastug.novashop.product;

import com.bastug.novashop.product.dto.ProductResponse;
import com.bastug.novashop.product.dto.ProductSaveRequest;
import com.bastug.novashop.product.entity.Product;
import com.bastug.novashop.product.mapper.ProductMapper;
import com.bastug.novashop.product.repository.ProductRepository;
import com.bastug.novashop.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductService productService;
    @Test
    void createProduct_shouldReturnProductResponse_whenRequestIsValid(){

        ProductSaveRequest productSaveRequest=new ProductSaveRequest(
                "product-name",
                "description",
                new BigDecimal(1000),
                "url_url_",
                100

        );
        ProductResponse productResponse=new ProductResponse(
                1L,
                "product-name",
                "description",
                new BigDecimal(1000),
                "url_url_",
                100,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Product product=new Product();
        product.setProductName("product-name");
        product.setProductDescription("description");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setProductPrice(BigDecimal.valueOf(1000));
        product.setImageUrl("url_url_");
        product.setId(1L);
        when(productMapper.toEntity(productSaveRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        assertThat(productService.createProduct(productSaveRequest)).isEqualTo(productResponse);

    }
}
