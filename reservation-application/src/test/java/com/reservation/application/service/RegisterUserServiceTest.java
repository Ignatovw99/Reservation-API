package com.reservation.application.service;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.in.RegisterUserUseCase;
import com.reservation.application.port.out.FindUserPort;
import com.reservation.application.port.out.PersistUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RegisterUserServiceTest {

    @TestConfiguration
    static class RegisterUserTestConfig {

        @Bean
        public FindUserPort findUserPort() {
            return Mockito.mock(FindUserPort.class);
        }

        @Bean
        public PersistUserPort persistUserPort() {
            return Mockito.mock(PersistUserPort.class);
        }

        @Bean
        public RegisterUserUseCase.UseCaseMapper mapper() {
            return Mockito.mock(RegisterUserUseCase.UseCaseMapper.class);
        }

        @Bean
        public PasswordEncoder encoder() {
            return Mockito.mock(PasswordEncoder.class);
        }

        @Bean
        public RegisterUserUseCase registerUserUseCase() {
            return new RegisterUserService(findUserPort(), persistUserPort(), encoder(), mapper());
        }
    }

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private FindUserPort findUserPort;

    @MockBean
    private PersistUserPort persistUserPort;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RegisterUserUseCase.UseCaseMapper mapper;

    private static final Long ID = 3214L;

    public static final String NAME = "John Terry";

    public static final String USERNAME = "terry123";

    public static final String EMAIL = "terry@gmail.com";

    public static final String PASSWORD = "MostStrongestPass123!";

    private static final String HASH_PASS = "DSAdi2e12pimedsdASD!2eqwdas@!#E";

    private AppUser appUser;

    public static AppUser createAppUser() {
        return AppUser.builder()
                    .name(NAME)
                    .username(USERNAME)
                    .email(EMAIL)
                    .password(PASSWORD)
                .build();
    }

    public static RegisterUserUseCase.Command createCommand() {
        return new RegisterUserUseCase.Command(
                NAME, USERNAME, EMAIL, PASSWORD
        );
    }

    @BeforeEach
    public void setup() {
        appUser = createAppUser();

        when(findUserPort.findUserByUsername(anyString()))
                .thenReturn(null);

        when(findUserPort.findUserByEmail(anyString()))
                .thenReturn(null);

        when(mapper.toDomainEntity(any(RegisterUserUseCase.Command.class)))
                .thenReturn(appUser);

        when(passwordEncoder.encode(anyString()))
                .thenReturn(HASH_PASS);

        when(persistUserPort.saveUser(any(AppUser.class)))
                .thenAnswer((invocation) -> {
                    AppUser user = invocation.getArgument(0, AppUser.class);
                    user.setId(ID);
                    return user;
                });
    }

    @Test
    public void register_whenUsernameAndEmailAreNull_shouldThrowException() {
        String username = null, email = null;
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, username, email, PASSWORD);

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.LOGIN_PARAMETER_INVALID);
    }

    @Test
    public void register_whenUsernameIsNullButEmailIsNot_shouldThrowException() {
        String username = null;
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, username, EMAIL, PASSWORD);

        assertDoesNotThrow(
                () -> registerUserUseCase.register(userCommand)
        );
    }

    @Test
    public void register_whenEmailIsNullButUsernameIsNot_shouldThrowException() {
        String email = null;
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, email, PASSWORD);

        assertDoesNotThrow(
                () -> registerUserUseCase.register(userCommand)
        );
    }

    @Test
    public void register_whenThereIsAUserAlreadyWithThisUsername_shouldThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, PASSWORD);

        when(findUserPort.findUserByUsername(anyString()))
                .thenReturn(appUser);

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.USERNAME_NOT_UNIQUE);

        verify(findUserPort).findUserByUsername(anyString());
    }

    @Test
    public void register_whenUsernameDoesNotExist_shouldNotThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, PASSWORD);

        when(findUserPort.findUserByUsername(anyString()))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> registerUserUseCase.register(userCommand)
        );

        verify(findUserPort).findUserByUsername(anyString());
    }

    @Test
    public void register_whenThereIsAUserAlreadyWithThisEmail_shouldThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, PASSWORD);

        when(findUserPort.findUserByEmail(anyString()))
                .thenReturn(appUser);

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.EMAIL_NOT_UNIQUE);

        verify(findUserPort).findUserByEmail(anyString());
    }

    @Test
    public void register_whenEmailDoesNotExist_shouldNotThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, PASSWORD);

        when(findUserPort.findUserByEmail(anyString()))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> registerUserUseCase.register(userCommand)
        );

        verify(findUserPort).findUserByEmail(anyString());
    }

    @Test
    public void register_whenThePasswordDoesNotContainUppercaseLetter_shouldThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, "lowercase123!");

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.PASSWORD_INVALID);
    }

    @Test
    public void register_whenThePasswordDoesNotContainLowercaseLetter_shouldThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, "UPPERCASE123!");

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.PASSWORD_INVALID);
    }

    @Test
    public void register_whenThePasswordDoesNotContainAnyDigit_shouldThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, "Password!");

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.PASSWORD_INVALID);
    }

    @Test
    public void register_whenThePasswordDoesNotContainAnySpecialSymbol_shouldThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, "WithoutSpecial1");

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.PASSWORD_INVALID);
    }

    @Test
    public void register_whenThePasswordLengthIsLessThanEightSymbols_shouldThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, "Asd123!");

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.PASSWORD_INVALID);
    }

    @Test
    public void register_whenThePasswordLengthIsGreaterThan35Symbols_shouldThrowException() {
        String pass = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAewqeqedqwdasdasdmasnfklsdnfklsdnflksndflkasndklmas2133123123123123123@!!!!!!!!!!!dasdasdDAWQD";
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, pass);

        RegisterUserUseCase.InvalidUserRegistrationException exception = assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> registerUserUseCase.register(userCommand)
        );

        assertThat(exception).hasMessage(RegisterUserUseCase.Constants.PASSWORD_INVALID);
    }

    @Test
    public void register_whenThePasswordIsValid_shouldNotThrowException() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, "ValidPassword123!@");

        assertDoesNotThrow(
                () -> registerUserUseCase.register(userCommand)
        );
    }

    @Test
    public void register_whenEverythingIsValid_shouldInvokePersistPort() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, PASSWORD);

        registerUserUseCase.register(userCommand);

        verify(persistUserPort).saveUser(any(AppUser.class));
    }

    @Test
    public void register_whenEverythingIsValid_shouldHashPassword() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, PASSWORD);

        AppUser actualUser = registerUserUseCase.register(userCommand);

        assertEquals(HASH_PASS, actualUser.getPassword());
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void register_whenEverythingIsValid_shouldReturnTheNewAppUser() {
        RegisterUserUseCase.Command userCommand =
                new RegisterUserUseCase.Command(NAME, USERNAME, EMAIL, PASSWORD);

        AppUser actualUser = registerUserUseCase.register(userCommand);

        assertAll(
                () -> assertNotNull(actualUser),
                () -> assertEquals(ID, actualUser.getId()),
                () -> assertEquals(appUser.getName(), actualUser.getName()),
                () -> assertEquals(appUser.getUsername(), actualUser.getUsername()),
                () -> assertEquals(appUser.getEmail(), actualUser.getEmail()),
                () -> assertEquals(HASH_PASS, actualUser.getPassword())
        );
    }
}
