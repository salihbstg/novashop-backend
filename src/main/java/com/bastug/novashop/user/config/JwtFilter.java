package com.bastug.novashop.user.config;

import com.bastug.novashop.user.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // JWT işlemlerini yapan servis (token parse etme, username/role çekme vs.)
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Gelen request'in URL path'i alınır
        String path = request.getRequestURI();

        // Auth endpointlerine gelen istekleri filtrelemeden geçiriyoruz
        // (login/register gibi endpointlerde JWT gerekmez)
        if (path.startsWith("/api/v1/auth/") && !path.startsWith("/api/v1/auth/change-password")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Request header içinden Authorization bilgisi alınır
        String authHeader = request.getHeader("Authorization");

        // Eğer token yoksa veya "Bearer " ile başlamıyorsa isteği olduğu gibi devam ettir
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer kısmını atıp sadece JWT token'ı alıyoruz
        String token = authHeader.substring(7);

        // Token içinden username (subject) bilgisi çekilir
        String username = jwtService.extractUsername(token);

        // Token içinden role bilgisi çekilir (USER, ADMIN vs.)
        String role = jwtService.extractRole(token);

        // Spring Security için Authentication objesi oluşturuluyor
        // 1. username
        // 2. password (JWT'de gerek yok o yüzden null)
        // 3. authority (role bilgisi)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );

        // Oluşturulan authentication Spring Security context'e set edilir
        // Böylece artık kullanıcı "login olmuş" olarak kabul edilir
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Filter chain devam ettirilir (request controller'a gider)
        filterChain.doFilter(request, response);
    }
}