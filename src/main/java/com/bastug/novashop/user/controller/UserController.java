package com.bastug.novashop.user.controller;

import com.bastug.novashop.user.dto.userdto.UpdateUserRequest;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import com.bastug.novashop.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    // User ile ilgili business işlemleri yapan service katmanı
    private final UserService userService;

    // Belirli bir username'e göre kullanıcı getirir (admin/debug amaçlı kullanılabilir)
    @GetMapping("{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username) {

        // path variable'dan gelen username service'e gönderilir
        // kullanıcı bulunursa response döner
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findByUsername(username));
    }

    // Login olmuş (JWT ile doğrulanmış) kullanıcının bilgilerini getirir
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {

        // SecurityContext içinden login olan kullanıcı alınır
        // sadece giriş yapan kullanıcı kendi bilgilerini görebilir
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateCurrentUser(request));
    }
}