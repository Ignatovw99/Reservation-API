package com.reservation.infrastructure.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.reservation.infrastructure.security.JwtUtil;
import com.reservation.infrastructure.security.SecurityProperties;
import com.reservation.infrastructure.security.SecurityUtil;
import com.reservation.infrastructure.security.model.LoginRequest;
import com.reservation.infrastructure.security.model.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final SecurityProperties securityProperties;

    private final Algorithm algorithm;

    public void setAuthenticationUrl(String authUrl) {
        log.info("Setting authentication url: {}", authUrl);
        this.setFilterProcessesUrl(authUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequest loginRequest = SecurityUtil.buildLoginRequest(request, securityProperties.getLogin());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        User user = SecurityUtil.extractUserFromAuthentication(authentication);
        if (Objects.isNull(user)) {
            SecurityUtil.buildUnsuccessfulLoginResponse(response);
            return;
        }
        SecurityProperties.Jwt propertiesJwt = securityProperties.getJwt();

        String issuer = request.getRequestURI();
        String accessToken = JwtUtil.generateAccessToken(user, issuer, algorithm, propertiesJwt);
        String refreshToken = JwtUtil.generateRefreshToken(user.getUsername(), issuer, algorithm, propertiesJwt.getRefreshToken());

        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);

        SecurityUtil.buildLoginResponse(response, loginResponse, propertiesJwt.getAccessToken(), propertiesJwt.getRefreshToken());
    }

//    TODO: implement refresh token functionality -> When should a refresh token be generated and send back to the user??

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//    TODO: here we can check for a brute force attack
        log.error("Unsuccessful authentication");
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
