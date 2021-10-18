package com.reservation.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

public final class JwtUtil {

    public static String generateAccessToken(User user, String issuer, Algorithm algorithm, SecurityProperties.Jwt jwtProperties) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getAccessToken().getExpiry() * 60 * 1000))
                .withIssuer(issuer)
                .withClaim(jwtProperties.getAuthoritiesClaim(), SecurityUtil.extractAuthoritiesFrom(user))
                .sign(algorithm);
    }

    public static String generateRefreshToken(String subject, String issuer, Algorithm algorithm, SecurityProperties.Jwt.Token refreshTokenProperties) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenProperties.getExpiry() * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public static String parseAuthJwt(HttpServletRequest request, final String tokenPrefix) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authHeader) || !authHeader.startsWith(tokenPrefix)) {
            return null;
        }
        return authHeader.substring(tokenPrefix.length());
    }

    public static DecodedJWT decodeJwt(String jwt, Algorithm algorithm) {
        return JWT.require(algorithm)
                .build()
                .verify(jwt);
    }
}
