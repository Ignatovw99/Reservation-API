package com.reservation.application.port.in;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.common.contract.CommandDomainEntityMapper;
import com.reservation.common.validation.SelfValidating;
import lombok.Getter;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface CreatePropertyTypeUseCase {

    PropertyType createPropertyType(Command propertyTypeCommand) throws NonUniquePropertyTypeNameException;

    @Getter
    @ToString
    final class Command extends SelfValidating<Command> {

        @NotNull
        @Size(min = 3, max = 255)
        private final String name;

        @NotNull
        private final Boolean requiresPrivate;

        @NotNull
        private final Boolean allowsMultipleRooms;

        @NotNull
        private final Boolean requiresAlternative;

        public Command(String name, Boolean requiresPrivate, Boolean allowsMultipleRooms, Boolean requiresAlternative) {
            this.name = name;
            this.requiresPrivate = requiresPrivate;
            this.allowsMultipleRooms = allowsMultipleRooms;
            this.requiresAlternative = requiresAlternative;
            super.validateSelf();
        }
    }

    final class NonUniquePropertyTypeNameException extends RuntimeException {

        private static final String MESSAGE = "The Property Type name should be unique. %s type already exists.";

        public NonUniquePropertyTypeNameException(String propertyTypeName) {
            super(String.format(MESSAGE, propertyTypeName));
        }
    }

    @Mapper(componentModel = "spring")
    interface UseCaseMapper extends CommandDomainEntityMapper<Command, PropertyType> {

        UseCaseMapper INSTANCE = Mappers.getMapper(UseCaseMapper.class);
    }
}
