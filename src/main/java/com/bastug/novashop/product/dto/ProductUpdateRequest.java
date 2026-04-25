package com.bastug.novashop.product.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductUpdateRequest (
        @NotNull(message = "Güncelleme için id vermelisiniz")
        Long id,
        String productName,
        String productDescription,
        @Positive(message = "Fiyat pozitif olmalıdır!")
        @NotNull(message = "Fiyat boş olamaz")
        BigDecimal productPrice,
        String imageUrl,
        Integer stock
){

}
