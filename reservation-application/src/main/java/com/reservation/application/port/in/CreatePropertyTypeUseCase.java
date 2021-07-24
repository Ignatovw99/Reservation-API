package com.reservation.application.port.in;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.common.validation.SelfValidating;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface CreatePropertyTypeUseCase {

    PropertyType createPropertyType(Command propertyTypeCommand) throws NonUniquePropertyTypeNameException;

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

        public String getName() {
            return name;
        }

        public Boolean getRequiresPrivate() {
            return requiresPrivate;
        }

        public Boolean getAllowsMultipleRooms() {
            return allowsMultipleRooms;
        }

        public Boolean getRequiresAlternative() {
            return requiresAlternative;
        }
    }

    final class NonUniquePropertyTypeNameException extends RuntimeException {

        private static final String MESSAGE = "The Property Type name should be unique. %s type already exists.";

        public NonUniquePropertyTypeNameException(String propertyTypeName) {
            super(String.format(MESSAGE, propertyTypeName));
        }
    }
}
