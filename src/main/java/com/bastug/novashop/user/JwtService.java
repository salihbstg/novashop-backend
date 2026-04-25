// JWT (JSON Web Token) üretme ve çözme işlemlerini yapan service sınıfı
// Kullanıcı login olduktan sonra token oluşturur ve gelen token'ı parse eder
package com.bastug.novashop.user;

import com.bastug.novashop.user.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // application.properties / yaml içinden secret key alınır
    @Value("${jwt.secret}")
    private String secret;

    // token geçerlilik süresi (ms cinsinden)
    @Value("${jwt.expiration}")
    private int expiration;

    // JWT imzalama için kullanılacak secret key oluşturulur
    public Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // JWT token üretme işlemi
    public String generateToken(String username, Role role) {
        return Jwts.builder()

                // Token içine username koyulur (subject = kimlik)
                .setSubject(username)

                // Custom claim: kullanıcı rolü eklenir (USER / ADMIN)
                .claim("role", role)

                // Token oluşturulma zamanı
                .setIssuedAt(new Date())

                // Token bitiş zamanı (expire süresi eklenir)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))

                // Token imzalanır (HS256 + secret key)
                .signWith(getKey(), SignatureAlgorithm.HS256)

                // String token olarak döner
                .compact();
    }

    //Refresh token üretimi
    public String generateRefreshToken(String username) {
        return Jwts.builder()

                // Token içine username koyulur (subject = kimlik)
                .setSubject(username)

                // Token oluşturulma zamanı
                .setIssuedAt(new Date())

                // Token bitiş zamanı (expire süresi eklenir)
                .setExpiration(new Date(System.currentTimeMillis() + expiration*10L))

                // Token imzalanır (HS256 + secret key)
                .signWith(getKey(), SignatureAlgorithm.HS256)

                // String token olarak döner
                .compact();
    }

    // Token içinden username (subject) bilgisi çekilir
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Token içinden role bilgisi çekilir
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                // role claim'i String olarak alınır
                .get("role", String.class);
    }

}