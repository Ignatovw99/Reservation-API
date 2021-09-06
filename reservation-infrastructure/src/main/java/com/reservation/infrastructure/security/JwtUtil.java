package com.reservation.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

public final class JwtUtil {

    private static final String AUTHORITIES_CLAIM = "authorities";

    public static String generateAccessToken(User user, String issuer, Algorithm algorithm) {
//        TODO: extract expires at to a config file / app properties
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim(AUTHORITIES_CLAIM, SecurityUtil.extractAuthoritiesFrom(user))
                .sign(algorithm);
    }

    public static String generateRefreshToken(String subject, String issuer, Algorithm algorithm) {
//        TODO: extract expires at to a config file / app properties
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
