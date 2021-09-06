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

//    TODO: extract this properties to a configuration file
    private static final String LOGIN_PARAM = "login";

    private static final String PASSWORD_PARAM = "password";

    private static final String ACCESS_TOKEN_HEADER = "Access-Token";

    private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    public static LoginRequest buildLoginRequest(HttpServletRequest request) {
//        TODO: add validation for empty and null
        String login = request.getParameter(LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        return new LoginRequest(login, password);
    }

    public static void buildLoginResponse(HttpServletResponse response, LoginResponse tokens) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

//        Headers
        response.setHeader(ACCESS_TOKEN_HEADER, tokens.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, tokens.getRefreshToken());

//        Body
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
