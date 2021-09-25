package com.reservation.infrastructure.security.model;

import lombok.Value;

import java.util.Objects;

@Value
public class LoginRequest {

    String login;

    String password;

    public boolean isValid() {
        return Objects.nonNull(login) && Objects.nonNull(password)
                && !(login.isEmpty() || password.isEmpty());
    }
}
