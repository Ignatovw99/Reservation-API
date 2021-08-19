package com.reservation.application.port.in;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.service.RegisterUserServiceTest;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserUseCaseTest {

    @Test
    public void testUseCaseMapper() {
        RegisterUserUseCase.Command command = RegisterUserServiceTest.createCommand();

        AppUser actual = RegisterUserUseCase.UseCaseMapper.INSTANCE.toDomainEntity(command);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertAll(
                        () -> assertNotNull(actual.getName()),
                        () -> assertEquals(command.getName(), actual.getName())
                ),
                () -> assertAll(
                        () -> assertNotNull(actual.getEmail()),
                        () -> assertEquals(command.getEmail(), actual.getEmail())
                ),
                () -> assertAll(
                        () -> assertNotNull(actual.getUsername()),
                        () -> assertEquals(command.getUsername(), actual.getUsername())
                ),
                () -> assertAll(
                        () -> assertNotNull(command.getPassword()),
                        () -> assertNull(actual.getPassword())
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithNullName_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new RegisterUserUseCase.Command(
                        null,
                        RegisterUserServiceTest.USERNAME,
                        RegisterUserServiceTest.EMAIL,
                        RegisterUserServiceTest.PASSWORD
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithNullUsernameAndEmail_shouldNotThrowException() {
        assertDoesNotThrow(
                () -> new RegisterUserUseCase.Command(
                        RegisterUserServiceTest.NAME,
                        null,
                        null,
                        RegisterUserServiceTest.PASSWORD
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithInvalidEmail_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new RegisterUserUseCase.Command(
                        RegisterUserServiceTest.NAME,
                        RegisterUserServiceTest.USERNAME,
                        "invalidEmail.bg",
                        RegisterUserServiceTest.PASSWORD
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithNullPassword_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new RegisterUserUseCase.Command(
                        RegisterUserServiceTest.NAME,
                        RegisterUserServiceTest.USERNAME,
                        RegisterUserServiceTest.EMAIL,
                        null
                )
        );
    }
}
