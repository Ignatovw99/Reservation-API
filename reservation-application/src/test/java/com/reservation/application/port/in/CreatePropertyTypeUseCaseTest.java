package com.reservation.application.port.in;

import com.reservation.application.domain.entity.PropertyType;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class CreatePropertyTypeUseCaseTest {

    private final CreatePropertyTypeUseCase.UseCaseMapper mapper = CreatePropertyTypeUseCase.UseCaseMapper.INSTANCE;

    private static final String NAME = "hotel";

    private static final Boolean REQUIRES_PRIVATE = true;

    private static final Boolean ALLOWS_MULTIPLE_ROOMS = true;

    private static final Boolean REQUIRES_ALTERNATIVE = false;

    @Test
    public void testUseCaseMapper_commandToDomainEntity_shouldHaveSameValues() {
        CreatePropertyTypeUseCase.Command command =
                new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE);

        PropertyType result = mapper.toDomainEntity(command);

        assertAll(
                () -> assertEquals(result.getName(), command.getName()),
                () -> assertEquals(result.getRequiresPrivate(), command.getRequiresPrivate()),
                () -> assertEquals(result.getAllowsMultipleRooms(), command.getAllowsMultipleRooms()),
                () -> assertEquals(result.getRequiresAlternative(), command.getRequiresAlternative())
        );
    }

    @Test
    public void testSelfValidating_whenCommandIsInitializedWithNameEqualsNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyTypeUseCase.Command(null, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE)
        );
    }

    @Test
    public void testSelfValidating_whenCommandIsInitializedWithNameLessThanThreeChars_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyTypeUseCase.Command("ab", REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE)
        );
    }

    @Test
    public void testSelfValidating_whenCommandIsInitializedWithRequiresPrivateEqualsNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyTypeUseCase.Command(NAME, null, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE)
        );
    }

    @Test
    public void testSelfValidating_whenCommandIsInitializedWithAllowsMultipleRoomsEqualsNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, null, REQUIRES_ALTERNATIVE)
        );
    }

    @Test
    public void testSelfValidating_whenCommandIsInitializedWithRequiresAlternativeEqualsNull_shouldThrowException() {
        assertThrows(
                ConstraintViolationException.class,
                () -> new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, null)
        );
    }

    @Test
    public void testSelfValidating_whenCommandIsInitializedWithoutNullValues_shouldThrowException() {
        assertDoesNotThrow(
                () -> new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE)
        );
    }
}
