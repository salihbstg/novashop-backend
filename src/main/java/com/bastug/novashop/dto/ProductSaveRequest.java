package com.bastug.novashop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record ProductSaveRequest(
        @NotNull(message = "Ürün adı boş olamaz") @NotEmpty(message = "Ürün adı boş olamaz")
        String productName,
        @NotNull(message = "Açıklama boş olamaz") @NotEmpty(message = "Açıklama boş olamaz")
        String productDescription,
        @NotNull(message = "Fiyat boş olamaz") @NotEmpty(message = "Fiyat boş olamaz")
        String productPrice,
        String imageUrl,
        Integer stock

) {
}
