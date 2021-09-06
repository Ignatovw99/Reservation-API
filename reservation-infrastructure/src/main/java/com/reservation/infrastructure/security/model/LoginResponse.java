package com.reservation.infrastructure.security.model;

import lombok.Value;

@Value
public class LoginResponse {

    String accessToken;

    String refreshToken;
}
