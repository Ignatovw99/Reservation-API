package com.reservation.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.reservation.*")
public class ReservationApiInfrastructureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationApiInfrastructureApplication.class, args);
    }

}
