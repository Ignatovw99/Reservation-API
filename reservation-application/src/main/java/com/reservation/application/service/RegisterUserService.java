package com.reservation.application.service;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.in.RegisterUserUseCase;
import com.reservation.application.port.out.FindUserPort;
import com.reservation.application.port.out.PersistUserPort;
import com.reservation.common.component.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UseCase
@RequiredArgsConstructor
@Slf4j
class RegisterUserService implements RegisterUserUseCase {

    private final FindUserPort findUserPort;

    private final PersistUserPort persistUserPort;

    private final PasswordEncoder passwordEncoder;

    private final UseCaseMapper mapper;

    @Override
    public AppUser register(Command userCommand) {
        log.info("Registering a new user: {}", userCommand);

        final String username = userCommand.getUsername();
        final String email = userCommand.getEmail();
        final String password = userCommand.getPassword();

        requireValidLogin(username, email);
        requireValidPassword(password, AppUser.PASSWORD_REGEX);
        requireUniqueUsername(username);
        requireUniqueEmail(email);

        AppUser appUser = mapper.toDomainEntity(userCommand);
        appUser.setPassword(passwordEncoder.encode(password));
        return persistUserPort.saveUser(appUser);
    }

    private void requireValidLogin(String username, String email) {
        boolean isLoginValid = Objects.nonNull(username) || Objects.nonNull(email);
        if (!isLoginValid) {
            log.error("Registering user failed, at least one of the username or email should not be null");
            throw new InvalidUserRegistrationException(Constants.LOGIN_PARAMETER_INVALID);
        }
    }

    private void requireValidPassword(String password, String passwordPattern) {
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            log.error("Registering user failed, password is not valid");
            throw new InvalidUserRegistrationException(Constants.PASSWORD_INVALID);
        }
    }

    private void requireUniqueUsername(String username) {
        AppUser userByUsername = findUserPort.findUserByUsername(username);
        if (Objects.nonNull(userByUsername)) {
            log.error("Registering user failed, username {} already exists", username);
            throw new InvalidUserRegistrationException(Constants.USERNAME_NOT_UNIQUE);
        }
    }

    private void requireUniqueEmail(String email) {
        AppUser userByEmail = findUserPort.findUserByEmail(email);
        if (Objects.nonNull(userByEmail)) {
            log.error("Registering user failed, email {} already exists", email);
            throw new InvalidUserRegistrationException(Constants.EMAIL_NOT_UNIQUE);
        }
    }
}
