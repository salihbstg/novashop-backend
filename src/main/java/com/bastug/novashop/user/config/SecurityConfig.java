package com.bastug.novashop.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Spring Security filtre zincirini tanımlar
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
                // CSRF korumasını, basichttp ve form logini kapatıyoruz
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                //Server tarafında oturum tutulmaz, her korumalı istekte token zorunlu
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Endpoint bazlı yetkilendirme kuralları
                .authorizeHttpRequests(auth -> auth

                        // Auth endpointleri herkes erişebilir (login/register)
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/refresh-token").permitAll()

                        //Şifre değişikliği yalnızca oturum açıldığında yapılabilir (ADMIN ve USER)
                        .requestMatchers("/api/v1/auth/change-password").hasAnyRole("ADMIN", "USER")

                        // Admin endpointleri sadece ADMIN rolüne sahip kullanıcılar erişebilir
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // User endpointleri USER ve ADMIN rolü olanlar erişebilir
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER","ADMIN")

                        // Diğer tüm endpointler login olmayı gerektirir
                        .anyRequest().authenticated()
                )

                // JWT filter'ı Spring Security filter zincirine ekliyoruz
                // UsernamePasswordAuthenticationFilter'dan önce çalışır
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Password şifreleme bean'i (BCrypt kullanılır)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}