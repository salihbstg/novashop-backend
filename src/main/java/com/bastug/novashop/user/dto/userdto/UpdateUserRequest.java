package com.bastug.novashop.user.dto.userdto;

import jakarta.validation.constraints.*;

public record UpdateUserRequest(
        @NotNull(message="Adınız boş olamaz")
        String firstName,
        @NotNull(message="Soyadınız boş olamaz")
        String lastName,
        @Pattern(regexp = "\\+90\\d{10}",
                message = "Hatalı telefon numarası formatı!"
        )
        String phone,
        @Pattern(
                regexp = "^[\\w._%+-]+@(gmail|hotmail|outlook)\\.com$",
                message = "Hatalı email formatı!"
        )
        String email,
        @NotNull(message="Adres boş olamaz")
        String address){}
