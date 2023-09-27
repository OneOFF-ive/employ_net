package com.five.employnet.common;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.expiration-time}")
    private long EXPIRATION_TIME;

    public String generateToken(String userId) {
        Date expirationDate = new Date(System.currentTimeMillis() + this.EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, this.SECRET_KEY)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
}
