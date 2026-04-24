package com.bastug.novashop.user.controller;

import com.bastug.novashop.user.dto.authdto.*;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import com.bastug.novashop.user.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    // Auth işlemlerini yapan service (register, login vs.)
    private final AuthService authService;

    // Kullanıcı kayıt endpointi
    @PostMapping("/register")
    ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {

        // registerRequest validasyonlardan geçerek gelir (@Valid)
        // servis katmanına gönderilir ve kullanıcı oluşturulur
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(registerRequest));
    }

    // Kullanıcı login endpointi
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        // login bilgileri service'e gönderilir
        // başarılıysa JWT token döner
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }

    //Refresh token ile yeni bir access token oluşturma
    @PostMapping("/refresh-token")
    ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/change-password")
    ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(authService.changePassword(changePasswordRequest));
    }
}