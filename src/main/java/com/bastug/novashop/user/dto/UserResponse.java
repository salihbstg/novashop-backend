package com.bastug.novashop.user.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String role
) {
}
