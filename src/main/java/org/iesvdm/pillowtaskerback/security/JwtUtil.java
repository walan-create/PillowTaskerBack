package org.iesvdm.pillowtaskerback.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.iesvdm.pillowtaskerback.domain.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    // private final String SECRET_KEY = "Xy4pX9d3Fg9sLm2D8yQs2Zp1rRit3Yj7Hk9Kr89lmBz6ZoM9s9Cv8j8L2hK";
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 horas

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getMail())
                .claim("name", user.getName())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String extractMail(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}

