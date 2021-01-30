package com.reservation.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<List<String>> demoHandler() {
        return ResponseEntity.of(Optional.of(List.of("Hello", "World", "I hope to be there!", "I am glad it works")));
    }
}
