package com.reservation.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.infrastructure.security.model.LoginRequest;
import com.reservation.infrastructure.security.model.LoginResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SecurityUtil {

    public static LoginRequest buildLoginRequest(HttpServletRequest request, SecurityProperties.Login loginProperties) {
//        TODO: add validation for empty and null
        String login = request.getParameter(loginProperties.getUsernameParameter());
        String password = request.getParameter(loginProperties.getPasswordParameter());
        return new LoginRequest(login, password);
    }

    public static void buildLoginResponse(HttpServletResponse response, LoginResponse tokens, SecurityProperties.Jwt.Token accessTokenProps, SecurityProperties.Jwt.Token refreshTokenProps) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

//        Headers
        response.setHeader(accessTokenProps.getHeader(), tokens.getAccessToken());
        response.setHeader(refreshTokenProps.getHeader(), tokens.getRefreshToken());

//        Body  -> the object can be HashMap too
        new ObjectMapper()
                .writeValue(response.getOutputStream(), tokens);
    }

    public static User extractUserFromAuthentication(Authentication authentication) {

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
//            TODO: throw an exception
            return new User("", "", new ArrayList<>());
        }
        return (User) principal;
    }

    public static List<String> extractAuthoritiesFrom(User user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableList());
    }
}
