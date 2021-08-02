package com.reservation.web.controller;

import com.reservation.common.contract.Identifiable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

public final class ApiController {

    public static <I, T extends Identifiable<I>> ResponseEntity<T> post(T response) {
        String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .getPath();

        location = Objects.nonNull(location) ? location : "";

        return ResponseEntity
                .created(URI.create(location))
                .body(response);
    }
}
