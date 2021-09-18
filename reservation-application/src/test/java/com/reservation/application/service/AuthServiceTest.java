package com.reservation.application.service;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.in.AuthUseCase;
import com.reservation.application.port.out.FindUserPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @TestConfiguration
    static class AuthServiceTestConfig {

        @Bean
        public FindUserPort findUserPort() {
            return Mockito.mock(FindUserPort.class);
        }

        @Bean
        public AuthUseCase authUseCase() {
            return new AuthService(findUserPort());
        }
    }

    @Autowired
    private AuthUseCase authUseCase;

    @MockBean
    private FindUserPort findUserPort;

    @Test
    public void loadUserByUsername_whenTheUserDoesNotExist_shouldThrowException() {
        String username = "invalid";
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> authUseCase.loadUserByUsername(username)
        );

        assertThat(exception)
                .hasMessage(String.format(AuthUseCase.Constants.USERNAME_DOES_NOT_EXIST, username));

        verify(findUserPort).findUserByLogin(anyString());
    }

    @Test
    public void loadUserByUsername_whenTheUserExists_shouldReturnThisUser() {
        AppUser appUser = RegisterUserServiceTest.createAppUser();

        when(findUserPort.findUserByLogin(anyString()))
                .thenReturn(appUser);

        UserDetails actual = authUseCase.loadUserByUsername(appUser.getLogin());

        assertNotNull(actual);
        assertEquals(appUser.getLogin(), actual.getUsername());

        verify(findUserPort).findUserByLogin(anyString());
    }

//    TODO: add more assertion according the result from loadUserByUsername method!!
//    TODO - seed the authorities of user
//    TODO - create the user with the builder pattern - think of adding a authorities class???
}
