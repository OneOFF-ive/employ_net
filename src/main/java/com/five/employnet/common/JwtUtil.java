package com.five.employnet.common;

import io.jsonwebtoken.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.Objects;

public class JwtUtil {

    private static final AnnotationConfigApplicationContext applicationContext = BaseContext.getApplicationContext();
    private static final String SECRET_KEY = applicationContext.getEnvironment().getProperty("employNet.SECRET_KEY");
    private static final long EXPIRATION_TIME = Long.parseLong(Objects.requireNonNull(applicationContext.getEnvironment().getProperty("employNet.EXPIRATION_TIME")));

    public static String generateToken(String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
}
