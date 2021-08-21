package com.reservation.application.port.in;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.common.contract.CommandDomainEntityMapper;
import com.reservation.common.validation.SelfValidating;
import lombok.Getter;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public interface RegisterUserUseCase {

    AppUser register(Command userCommand);

    @Getter
    @ToString
    final class Command extends SelfValidating<Command> {

        @NotNull
        private final String name;

        private final String username;

        @Pattern(regexp = "[a-zA-Z0-9_]+@[a-z]+\\.[a-z]{2,3}", message = "Email invalid")
        private final String email;

        @NotNull
        @ToString.Exclude
        private final String password;

        public Command(String name, String username, String email, String password) {
            this.name = name;
            this.username = username;
            this.email = email;
            this.password = password;
            super.validateSelf();
        }
    }

    @Getter
    final class InvalidUserRegistrationException extends RuntimeException {

        public InvalidUserRegistrationException(String message) {
            super(message);
        }
    }

    final class Constants {

        public static final String LOGIN_PARAMETER_INVALID = "At least one of the email or username should not be null";

        public static final String USERNAME_NOT_UNIQUE = "Someone has already registered with this username";

        public static final String EMAIL_NOT_UNIQUE = "Someone has already registered with this email";

        public static final String PASSWORD_INVALID = "Password should contain at least one uppercase, one lowercase, one digit and one special symbol";
    }

    @Mapper(componentModel = "spring")
    interface UseCaseMapper extends CommandDomainEntityMapper<Command, AppUser> {

        UseCaseMapper INSTANCE = Mappers.getMapper(UseCaseMapper.class);

        @Override
        @Mapping(target = "password", ignore = true)
        AppUser toDomainEntity(Command command);
    }
}
