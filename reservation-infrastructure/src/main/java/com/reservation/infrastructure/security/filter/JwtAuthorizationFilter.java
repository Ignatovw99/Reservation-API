package com.reservation.infrastructure.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.reservation.infrastructure.security.JwtUtil;
import com.reservation.infrastructure.security.SecurityProperties;
import com.reservation.infrastructure.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final Algorithm algorithm;

    private void authorizeRequest(HttpServletRequest request, String jwt) {
        DecodedJWT decodedJwt = JwtUtil.decodeJwt(jwt, algorithm);
        String login = decodedJwt.getSubject();
        String[] roles = decodedJwt.getClaim(securityProperties.getJwt().getAuthoritiesClaim()).asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role))
        );
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("Authorizing request");

        final String loginUrl = securityProperties.getLogin().getUrl();
        if (!request.getServletPath().equals(loginUrl)) {
            String jwt = JwtUtil.parseAuthJwt(request, securityProperties.getJwt().getPrefixHeader());
            try {
                authorizeRequest(request, jwt);
            } catch (NullPointerException ex) {
                log.error("Cannot set user authentication - Authorization header not valid : {}", ex.getMessage());
                SecurityUtil.handleAuthorizationError(response, "Authorization header invalid", securityProperties.getAuthorizationErrorHeader());
                return;
            } catch (JWTVerificationException ex) {
                log.error("Cannot set user authentication - JWT not valid: {}", ex.getMessage());
                SecurityUtil.handleAuthorizationError(response, ex.getMessage(), securityProperties.getAuthorizationErrorHeader());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
