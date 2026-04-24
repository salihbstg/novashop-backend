// Kullanıcı ile ilgili business işlemleri yapan service sınıfı
// User verisini DB'den çekme ve DTO'ya çevirme işlemlerini yapar
package com.bastug.novashop.user.service;

import com.bastug.novashop.user.dto.userdto.UpdateUserRequest;
import com.bastug.novashop.user.dto.userdto.UserResponse;
import com.bastug.novashop.user.entity.User;
import com.bastug.novashop.user.exception.ApplicationExceptionImpl;
import com.bastug.novashop.user.mapper.UserMapper;
import com.bastug.novashop.user.repository.UserRepository;
import com.bastug.novashop.user.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // DB erişimi (user tablosu işlemleri)
    private final UserRepository userRepository;

    // Entity ↔ DTO dönüşümleri
    private final UserMapper userMapper;

    // Username'e göre kullanıcı getirme
    @Override
    public UserResponse findByUsername(String username) {

        // DB'den kullanıcı aranır
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return userMapper.toUserResponse(user);
        }
        throw new ApplicationExceptionImpl("Kullanıcı bulunamadı!");

    }

    // Login olmuş (JWT ile doğrulanmış) kullanıcıyı getirir
    @Override
    public UserResponse getCurrentUser() {

        // SecurityContext içinden login olmuş kullanıcının username'i alınır
        // JWT filter bu bilgiyi daha önce SecurityContext'e koymuştu
        String username = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()
        ).getName();

        // username ile DB'den user bulunur
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // DTO'ya çevrilip response olarak döner
            return userMapper.toUserResponse(user);
        }
        throw new ApplicationExceptionImpl("Kullanıcı bulunamadı!");

    }

    // SecurityContext içinden login olmuş kullanıcının username'i alınır
    // username 'i alınan kullanıcı güncellenir ve sonrasında UserResponse döner, token olmadan istek atılamayacağından kullanıcı adını body içerisinde almaya gerek yok.
    @Override
    @Transactional
    public UserResponse updateCurrentUser(UpdateUserRequest request) {

        String username = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()
        ).getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Email daha önce kullanılmış mı kontrolü
            if (userRepository.existsByEmail(request.email())
                    && !user.getEmail().equals(request.email()))
                throw new ApplicationExceptionImpl("Mail adresi zaten kayıtlı");


            // Telefon numarası daha önce kullanılmış mı kontrolü
            if (userRepository.existsByPhone(request.phone())
                    && !user.getPhone().equals(request.phone()))
                throw new ApplicationExceptionImpl("Telefon numarası zaten kayıtlı!");

            user.setEmail(request.email());
            user.setAddress(request.address());
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setPhone(request.phone());

            userRepository.save(user);
            return userMapper.toUserResponse(user);
        }
        throw new ApplicationExceptionImpl("Kullanıcı bulunamadı!");

    }
}























