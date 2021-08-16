package com.reservation.application.port.in;

import com.reservation.application.domain.entity.Property;
import com.reservation.common.contract.CommandDomainEntityMapper;
import com.reservation.common.validation.SelfValidating;
import lombok.Getter;
import lombok.ToString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.validation.constraints.*;

public interface CreatePropertyUseCase {

    Property createProperty(Command propertyCommand);

    @Getter
    @ToString
    final class Command extends SelfValidating<Command> {

        @NotNull
        @Size(min = 4)
        private final String name;

        @NotNull
        @Min(1)
        @Max(5)
        private final Byte starRatings;

        @NotNull
        private final Long propertyTypeId;

        @NotNull
        @Size(min = 10, max = 2000)
        private final String description;

        @NotNull
        private final String country;

        private final String region;

        private final String street;

        private final Integer houseNumber;

        private final String zip;

        private final String contactName;

        private final String contactNumber;

        @NotNull
        @Pattern(regexp = "[a-zA-Z0-9_]+@[a-z]+\\.[a-z]{2,3}", message = "Email invalid")
        private final String contactEmail;

        public Command(String name,
                       Byte starRatings,
                       Long propertyTypeId,
                       String description,
                       String country,
                       String region,
                       String street,
                       Integer houseNumber,
                       String zip,
                       String contactName,
                       String contactNumber,
                       String contactEmail) {
            this.name = name;
            this.starRatings = starRatings;
            this.propertyTypeId = propertyTypeId;
            this.description = description;
            this.country = country;
            this.region = region;
            this.street = street;
            this.houseNumber = houseNumber;
            this.zip = zip;
            this.contactName = contactName;
            this.contactNumber = contactNumber;
            this.contactEmail = contactEmail;
            super.validateSelf();
        }
    }

    @Getter
    class PropertyInvalidCommandException extends RuntimeException {

        private final boolean isConflict;

        private PropertyInvalidCommandException(String message, boolean isConflict) {
            super(message);
            this.isConflict = isConflict;
        }

        public static final class Conflict extends PropertyInvalidCommandException {

            public Conflict(String message) {
                super(message, true);
            }
        }

        public static final class BadRequest extends PropertyInvalidCommandException {

            public BadRequest(String message) {
                super(message, false);
            }
        }
    }

    @Mapper(componentModel = "spring")
    interface UseCaseMapper extends CommandDomainEntityMapper<Command, Property> {

        UseCaseMapper INSTANCE = Mappers.getMapper(UseCaseMapper.class);

        @Override
        @Mapping(source = "propertyTypeId", target = "type.id")
        Property toDomainEntity(Command command);
    }
}
