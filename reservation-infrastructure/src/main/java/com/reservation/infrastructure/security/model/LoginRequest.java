package com.reservation.infrastructure.security.model;

import lombok.Value;

@Value
public class LoginRequest {

    String login;

    String password;
}
