package com.bastug.novashop.user.dto.authdto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank String oldPassword,
        @NotBlank(message = "Yeni şifre boş olamaz")
        String newPassword,
        @NotBlank(message = "Yeni şifre tekrarı boş olamaz")
        String confirmPassword
) {
}
