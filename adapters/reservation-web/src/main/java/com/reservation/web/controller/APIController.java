package com.reservation.web.controller;

import org.springframework.http.ResponseEntity;

import java.net.URI;

public final class APIController {

    public static <T> ResponseEntity<T> post(T response, String createdURI, Object... args) {
        return ResponseEntity
                .created(URI.create(String.format(createdURI, args)))
                .body(response);
    }
}
