package com.bastug.novashop.user.dto;
import jakarta.validation.constraints.*;
public record RegisterRequest(

        @NotNull(message="Kullanıcı adı boş olamaz")
        @Pattern(
                regexp="^[a-zA-Z]{6,}$",
                message="Kullanıcı adı en az 6 harfli olmalıdır!"
        )
        String username,
        @NotNull(message="Şifre boş olamaz")
        String password,
        @Pattern(
                regexp = "^[\\w._%+-]+@(gmail|hotmail|outlook)\\.com$",
                message = "Hatalı email formatı!"
        )
        String email,
        @NotNull(message="Adınız boş olamaz")
        String firstName,
        @NotNull(message="Soyadınız boş olamaz")
        String lastName,
        @Pattern(regexp = "\\+90\\d{10}",
                message = "Hatalı telefon numarası formatı!"
        )
        String phone,
        @NotNull(message="Adres boş olamaz")
        String address) {


}
