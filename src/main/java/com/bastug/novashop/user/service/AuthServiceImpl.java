// Auth (kimlik doğrulama) işlemlerini yapan service sınıfı
// Kullanıcı kayıt (register) ve giriş (login) işlemlerini yönetir
package com.bastug.novashop.user.service;

import com.bastug.novashop.user.config.SecurityConfig;
import com.bastug.novashop.user.dto.LoginRequest;
import com.bastug.novashop.user.dto.RegisterRequest;
import com.bastug.novashop.user.dto.UserResponse;
import com.bastug.novashop.user.entity.User;
import com.bastug.novashop.user.enums.Role;
import com.bastug.novashop.user.exception.ApplicationExceptionHandler;
import com.bastug.novashop.user.exception.ApplicationExceptionImpl;
import com.bastug.novashop.user.mapper.UserMapper;
import com.bastug.novashop.user.repository.UserRepository;
import com.bastug.novashop.user.service.interfaces.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // User database işlemleri (CRUD)
    private final UserRepository userRepository;

    // DTO ↔ Entity dönüşümleri
    private final UserMapper userMapper;

    // Password encoder ve security config erişimi
    private final SecurityConfig securityConfig;

    // JWT token üretme ve doğrulama işlemleri
    private final JwtService jwtService;

    // Üyelik oluşturma (kullanıcı kayıt işlemi)
    @Transactional
    @Override
    public UserResponse register(RegisterRequest registerRequest) {

        // Email daha önce kullanılmış mı kontrolü
        if(userRepository.existsByEmail(registerRequest.email()))
            throw new ApplicationExceptionImpl("Mail adresi zaten kayıtlı");

        // Username daha önce kullanılmış mı kontrolü
        if(userRepository.existsByUsername(registerRequest.username()))
            throw new ApplicationExceptionImpl("Kullanıcı adı zaten kayıtlı!");

        // Telefon numarası daha önce kullanılmış mı kontrolü
        if(userRepository.existsByPhone(registerRequest.phone()))
            throw new ApplicationExceptionImpl("Telefon numarası zaten kayıtlı!");

        // DTO → Entity dönüşümü
        User user = userMapper.toEntity(registerRequest);

        // Şifre BCrypt ile hashlenir (güvenlik için düz text saklanmaz)
        user.setPassword(securityConfig.passwordEncoder().encode(registerRequest.password()));

        // Kullanıcı DB'ye kaydedilir
        userRepository.save(user);

        // Response DTO olarak geri döner
        return userMapper.toUserResponse(user);
    }

    // Kullanıcı login işlemi
    @Override
    public String login(LoginRequest loginRequest) {

        // Username ile kullanıcı bulunur
        User user = userRepository.findByUsername(loginRequest.getUsername());

        // Kullanıcı yoksa hata fırlatılır
        if(user == null)
            throw new ApplicationExceptionImpl("Hatalı kullanıcı adı!");

        // Şifre kontrolü (BCrypt matches kullanılır)
        if(!securityConfig.passwordEncoder().matches(
                loginRequest.getPassword(),
                user.getPassword()
        ))
            throw new ApplicationExceptionImpl("Hatalı şifre!");

        // Başarılı login → JWT token üretilir
        return jwtService.generateToken(user.getUsername(), user.getRole());
    }
}