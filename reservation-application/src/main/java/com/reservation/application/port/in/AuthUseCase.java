package com.reservation.application.port.in;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * This is the Authentication and Authorization use case contract
 */
public interface AuthUseCase extends UserDetailsService {


    final class Constants {

        public static final String USERNAME_DOES_NOT_EXIST = "User with %s username does not exist";
    }
}
