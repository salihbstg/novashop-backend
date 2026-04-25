// Auth (kimlik doğrulama) işlemlerini yapan service sınıfı
// Kullanıcı kayıt (register) ve giriş (login) işlemlerini yönetir
package com.bastug.novashop.user.service;

import com.bastug.novashop.user.dto.authdto.*;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import com.bastug.novashop.user.entity.User;
import com.bastug.novashop.exception.ApplicationExceptionImpl;
import com.bastug.novashop.user.mapper.UserMapper;
import com.bastug.novashop.user.repository.UserRepository;
import com.bastug.novashop.user.service.interfaces.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // User database işlemleri (CRUD)
    private final UserRepository userRepository;

    // DTO ↔ Entity dönüşümleri
    private final UserMapper userMapper;

    // Password encoder ve security config erişimi
    private final PasswordEncoder passwordEncoder;

    // JWT token üretme ve doğrulama işlemleri
    private final JwtService jwtService;

    @Value("${jwt.expiration}")
    private int expiration;

    // Üyelik oluşturma (kullanıcı kayıt işlemi)
    @Transactional
    @Override
    public UserResponse register(RegisterRequest registerRequest) {

        // Email daha önce kullanılmış mı kontrolü
        if (userRepository.existsByEmail(registerRequest.email()))
            throw new ApplicationExceptionImpl("Mail adresi zaten kayıtlı");

        // Username daha önce kullanılmış mı kontrolü
        if (userRepository.existsByUsername(registerRequest.username()))
            throw new ApplicationExceptionImpl("Kullanıcı adı zaten kayıtlı!");

        // Telefon numarası daha önce kullanılmış mı kontrolü
        if (userRepository.existsByPhone(registerRequest.phone()))
            throw new ApplicationExceptionImpl("Telefon numarası zaten kayıtlı!");

        // DTO → Entity dönüşümü
        User user = userMapper.toEntity(registerRequest);

        // Şifre BCrypt ile hashlenir
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        // Kullanıcı DB'ye kaydedilir
        userRepository.save(user);

        // Response DTO olarak geri döner
        return userMapper.toUserResponse(user);
    }

    // Kullanıcı login işlemi
    @Override
    public AuthResponse login(LoginRequest loginRequest) {


        // Username ile kullanıcı bulunur
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if(!optionalUser.isPresent()) {
            throw new ApplicationExceptionImpl("Kullanıcı bulunamadı");
        }
        User user = optionalUser.get();
        if (!user.getEnabled()) {
            throw new ApplicationExceptionImpl("Hesap deaktif, lütfen yetkili ile iletişime geçin!");
        }



        // Şifre kontrolü (BCrypt matches kullanılır)
        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        ))
            throw new ApplicationExceptionImpl("Hatalı şifre!");

        // Başarılı login → JWT token ve Refresh Token üretilir
        return new AuthResponse(
                jwtService.generateToken(user.getUsername(), user.getRole()),
                jwtService.generateRefreshToken(user.getUsername()),
                "Bearer",
                expiration

        );
    }

    //Refresh token ile yeni access token oluşturma
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshToken) {
        if (refreshToken == null)
            throw new ApplicationExceptionImpl("Token boş olamaz!");
// Username ile kullanıcı bulunur

        String username=jwtService.extractUsername(refreshToken.refreshToken());
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isPresent()) {
            throw new ApplicationExceptionImpl("Kullanıcı bulunamadı");
        }
        User user = optionalUser.get();

        if (jwtService.extractRole(refreshToken.refreshToken()) != null)
            throw new ApplicationExceptionImpl("Hatalı token!");
        if (!user.getEnabled()) {
            throw new ApplicationExceptionImpl("Hesap deaktif, lütfen yetkili ile iletişime geçin!");
        }

        return new AuthResponse(
                jwtService.generateToken(user.getUsername(), user.getRole()),
                jwtService.generateRefreshToken(user.getUsername()),
                "Bearer",
                expiration
        );
    }

    @Override
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isPresent()) {
            throw new ApplicationExceptionImpl("Kullanıcı bulunamadı!");
        }
        User user = optionalUser.get();
        if (passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())
                && changePasswordRequest.newPassword().equals(changePasswordRequest.confirmPassword())
        ) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
            userRepository.save(user);
            return "Şifre başarıyla değiştirildi.";
        }
        throw new ApplicationExceptionImpl("Şifre değiştirme başarısız, lütfen bilgileri kontrol ediniz.");

    }
}