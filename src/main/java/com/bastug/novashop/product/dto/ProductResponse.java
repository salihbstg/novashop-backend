package com.bastug.novashop.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String productName,
        String productDescription,
        BigDecimal productPrice,
        String imageUrl,
        int stock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
