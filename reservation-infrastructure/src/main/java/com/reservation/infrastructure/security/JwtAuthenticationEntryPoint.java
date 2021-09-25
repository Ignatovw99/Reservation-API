package com.reservation.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String authenticationErrorHeader;

    //This method will be triggered anytime unauthenticated User requests a secured HTTP resource and an AuthenticationException is thrown.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthenticated error: {}", authException.getMessage());
        SecurityUtil.handleAuthenticationError(response, authException.getMessage(), authenticationErrorHeader);
    }
}
