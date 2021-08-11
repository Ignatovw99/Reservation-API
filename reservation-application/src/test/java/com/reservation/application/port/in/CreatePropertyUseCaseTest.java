package com.reservation.application.port.in;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.service.CreatePropertyServiceTest;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatePropertyUseCaseTest {

    @Test
    public void testUseCaseMapper() {
        CreatePropertyUseCase.Command command = new CreatePropertyUseCase.Command(
                CreatePropertyServiceTest.name,
                CreatePropertyServiceTest.starRatings,
                CreatePropertyServiceTest.propertyTypeId,
                CreatePropertyServiceTest.description,
                CreatePropertyServiceTest.country,
                CreatePropertyServiceTest.region,
                CreatePropertyServiceTest.street,
                CreatePropertyServiceTest.houseNumber,
                CreatePropertyServiceTest.zip,
                CreatePropertyServiceTest.contactName,
                CreatePropertyServiceTest.contactNumber,
                CreatePropertyServiceTest.contactEmail
        );

        Property actualResult = CreatePropertyUseCase.UseCaseMapper.INSTANCE
                .toDomainEntity(command);

        assertAll(
                () -> assertEquals(command.getName(), actualResult.getName()),
                () -> assertEquals(command.getStarRatings(), actualResult.getStarRatings()),
                () -> assertEquals(command.getPropertyTypeId(), actualResult.getType().getId()),
                () -> assertEquals(command.getDescription(), actualResult.getDescription()),
                () -> assertEquals(command.getCountry(), actualResult.getCountry()),
                () -> assertEquals(command.getRegion(), actualResult.getRegion()),
                () -> assertEquals(command.getStreet(), actualResult.getStreet()),
                () -> assertEquals(command.getHouseNumber(), actualResult.getHouseNumber()),
                () -> assertEquals(command.getZip(), actualResult.getZip()),
                () -> assertEquals(command.getContactName(), actualResult.getContactName()),
                () -> assertEquals(command.getContactNumber(), actualResult.getContactNumber()),
                () -> assertEquals(command.getContactEmail(), actualResult.getContactEmail())
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithNullName_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        null,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithNameLengthLessThan4Symbols_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        "abc",
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithNullStarRatings_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        null,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithStarRatingsLessThan1_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        (byte) 0,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithStarRatingsGreaterThan5_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        (byte) 6,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithPropertyTypeIdNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        null,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithDescriptionNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        null,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithDescriptionLengthLessThan10Symbols_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        "ASD",
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithDescriptionLengthGreaterThan2000Symbols_shouldThrowException() {
        String s = Stream.generate(() -> "a")
                .limit(2001)
                .reduce((a, b) -> a + b).get();

        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        s,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithCountryNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        null,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithAllAllowedNullValues_shouldNotThrowException() {
        assertDoesNotThrow(
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithContactEmailNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        null
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithContactEmailWithoutAt_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        "invalid mail"
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithContactEmailInvalidDomain_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        "invalid@321.com"
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedWithContactEmailInvalidExtention_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        "invalid@domain.comads"
                )
        );
    }

    @Test
    public void selfValidating_whenCommandIsInitializedCorrectly_shouldNotThrowException() {
        assertDoesNotThrow(
                () -> new CreatePropertyUseCase.Command(
                        CreatePropertyServiceTest.name,
                        CreatePropertyServiceTest.starRatings,
                        CreatePropertyServiceTest.propertyTypeId,
                        CreatePropertyServiceTest.description,
                        CreatePropertyServiceTest.country,
                        CreatePropertyServiceTest.region,
                        CreatePropertyServiceTest.street,
                        CreatePropertyServiceTest.houseNumber,
                        CreatePropertyServiceTest.zip,
                        CreatePropertyServiceTest.contactName,
                        CreatePropertyServiceTest.contactNumber,
                        CreatePropertyServiceTest.contactEmail
                )
        );
    }
}
