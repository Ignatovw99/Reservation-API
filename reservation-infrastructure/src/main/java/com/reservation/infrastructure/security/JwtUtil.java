package com.reservation.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

public final class JwtUtil {

    public static String generateAccessToken(User user, String issuer, Algorithm algorithm, SecurityProperties.Jwt jwtProperties) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getAccessToken().getExpiry() * 60 * 1000))
                .withIssuer(issuer)
                .withClaim(jwtProperties.getAuthoritiesClaim(), SecurityUtil.extractAuthoritiesFrom(user))
                .sign(algorithm);
    }

    public static String generateRefreshToken(String subject, String issuer, Algorithm algorithm, SecurityProperties.Jwt.Token refreshTokenProperties) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenProperties.getExpiry() * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
