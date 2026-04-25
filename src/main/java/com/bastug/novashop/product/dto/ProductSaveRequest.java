package com.bastug.novashop.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record ProductSaveRequest(
        @NotNull(message = "Ürün adı boş olamaz") @NotEmpty(message = "Ürün adı boş olamaz")
        String productName,
        @NotNull(message = "Açıklama boş olamaz") @NotEmpty(message = "Açıklama boş olamaz")
        String productDescription,
        BigDecimal productPrice,
        String imageUrl,
        Integer stock

) {
}
