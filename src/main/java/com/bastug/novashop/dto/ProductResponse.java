package com.bastug.novashop.dto;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String productName,
        String productDescription,
        String productPrice,
        String imageUrl,
        String stock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
