package com.bastug.novashop.user.controller;

import com.bastug.novashop.user.dto.LoginRequest;
import com.bastug.novashop.user.dto.RegisterRequest;
import com.bastug.novashop.user.dto.UserResponse;
import com.bastug.novashop.user.service.interfaces.AuthService;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        // login bilgileri service'e gönderilir
        // başarılıysa JWT token döner
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }
}