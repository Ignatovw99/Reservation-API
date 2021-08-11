package com.reservation.application.service;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.application.port.out.FindPropertyTypeByNamePort;
import com.reservation.application.port.out.PersistPropertyTypePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CreatePropertyTypeServiceTest {

    @TestConfiguration
    static class CreatePropertyTypeConfig {

        @Bean
        public PersistPropertyTypePort persistPropertyTypePort() {
            return mock(PersistPropertyTypePort.class);
        }

        @Bean
        public FindPropertyTypeByNamePort findPropertyTypeByNamePort() {
            return mock(FindPropertyTypeByNamePort.class);
        }

        @Bean
        public CreatePropertyTypeUseCase.UseCaseMapper mapper() {
            return mock(CreatePropertyTypeUseCase.UseCaseMapper.class);
        }

        @Bean
        public CreatePropertyTypeUseCase createPropertyTypeUseCase() {
            return new CreatePropertyTypeService(persistPropertyTypePort(), findPropertyTypeByNamePort(), mapper());
        }
    }

    @Autowired
    private CreatePropertyTypeUseCase createPropertyTypeUseCase;

    @MockBean
    private PersistPropertyTypePort savePropertyTypePort;

    @MockBean
    private FindPropertyTypeByNamePort findPropertyTypeByNamePort;

    @MockBean
    private CreatePropertyTypeUseCase.UseCaseMapper mapper;

    private static final String NAME = "hotel";

    private static final Boolean REQUIRES_PRIVATE = true;

    private static final Boolean ALLOWS_MULTIPLE_ROOMS = true;

    private static final Boolean REQUIRES_ALTERNATIVE = false;

    private CreatePropertyTypeUseCase.Command command;

    private PropertyType propertyType;

    public static PropertyType createPropertyType() {
        return PropertyType.builder()
                    .name(NAME)
                    .allowsMultipleRooms(ALLOWS_MULTIPLE_ROOMS)
                    .requiresPrivate(REQUIRES_PRIVATE)
                    .requiresAlternative(REQUIRES_ALTERNATIVE)
                .build();
    }

    @BeforeEach
    public void setup() {
        command = new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE);
        propertyType = createPropertyType();
        when(mapper.toDomainEntity(any(CreatePropertyTypeUseCase.Command.class)))
                .thenReturn(propertyType);
    }

    @Test
    public void createPropertyType_whenThereIsAlreadyTypeWithTheGivenName_shouldThrowException() {
        when(findPropertyTypeByNamePort.findPropertyTypeByName(anyString()))
                .thenReturn(propertyType);

        assertThrows(
                CreatePropertyTypeUseCase.NonUniquePropertyTypeNameException.class,
                () ->  createPropertyTypeUseCase.createPropertyType(command)
        );
    }


    @Test
    public void createPropertyType_whenPropertyTypeWithGivenNameDoesNotExist_shouldNotThrowException() {
        when(findPropertyTypeByNamePort.findPropertyTypeByName(anyString()))
                .thenReturn(null);

        assertDoesNotThrow(() ->  createPropertyTypeUseCase.createPropertyType(command));
    }

    @Test
    public void createPropertyType_whenPropertyTypeIsValid_shouldReturnPersistedType() {
        final Long id = 254L;

        when(findPropertyTypeByNamePort.findPropertyTypeByName(anyString()))
                .thenReturn(null);

        when(savePropertyTypePort.savePropertyType(any(PropertyType.class)))
                .thenAnswer(invocation -> {
                    PropertyType propertyType = invocation.getArgument(0);
                    propertyType.setId(id);
                    return propertyType;
                });

        PropertyType expectedPropType = createPropertyTypeUseCase.createPropertyType(command);

        assertAll(
                () -> assertEquals(expectedPropType.getId(), id),
                () -> assertEquals(expectedPropType.getRequiresPrivate(), command.getRequiresPrivate()),
                () -> assertEquals(expectedPropType.getAllowsMultipleRooms(), command.getAllowsMultipleRooms()),
                () -> assertEquals(expectedPropType.getRequiresAlternative(), command.getRequiresAlternative())
        );
    }
}
