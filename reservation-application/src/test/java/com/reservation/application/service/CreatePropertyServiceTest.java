package com.reservation.application.service;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyUseCase;
import com.reservation.application.port.out.CountryValidatorPort;
import com.reservation.application.port.out.FindPropertyByNamePort;
import com.reservation.application.port.out.FindPropertyTypeByIdPort;
import com.reservation.application.port.out.PersistPropertyPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CreatePropertyServiceTest {

    @TestConfiguration
    static class CreatePropertyConfig {

        @Bean
        public FindPropertyByNamePort findPropertyByNamePort() {
            return Mockito.mock(FindPropertyByNamePort.class);
        }

        @Bean
        public CountryValidatorPort countryValidatorPort() {
            return Mockito.mock(CountryValidatorPort.class);
        }

        @Bean
        public FindPropertyTypeByIdPort findPropertyTypeByIdPort() {
            return Mockito.mock(FindPropertyTypeByIdPort.class);
        }

        @Bean
        public PersistPropertyPort persistPropertyPort() {
            return Mockito.mock(PersistPropertyPort.class);
        }

        @Bean
        public CreatePropertyUseCase.UseCaseMapper useCaseMapper() {
            return Mockito.mock(CreatePropertyUseCase.UseCaseMapper.class);
        }

        @Bean
        public CreatePropertyUseCase createPropertyUseCase() {
            return new CreatePropertyService(
                    findPropertyByNamePort(),
                    countryValidatorPort(),
                    findPropertyTypeByIdPort(),
                    persistPropertyPort(),
                    useCaseMapper()
            );
        }
    }

    public static final String name = "Hilton";

    public static final Byte starRatings = 5;

    public static final Long propertyTypeId = 3L;

    public static final String description = "This is an amazing hotel";

    public static final String country = "USA";

    public static final String region = "Las Vegas";

    public static final String street = "John Smith";

    public static final Integer houseNumber = 43;

    public static final String zip = "4567";

    public static final String contactName = "Amanda Johnson";

    public static final String contactNumber = "+3244124235234";

    public static final String contactEmail = "hilton@mail.com";

    @Autowired
    private CreatePropertyUseCase createPropertyUseCase;

    @MockBean
    private FindPropertyByNamePort findPropertyByNamePort;

    @MockBean
    private CountryValidatorPort countryValidatorPort;

    @MockBean
    private FindPropertyTypeByIdPort findPropertyTypeByIdPort;

    @MockBean
    private PersistPropertyPort persistPropertyPort;

    @MockBean
    private CreatePropertyUseCase.UseCaseMapper useCaseMapper;

    private CreatePropertyUseCase.Command createCommand() {
        return new CreatePropertyUseCase.Command(
                name,
                starRatings,
                propertyTypeId,
                description,
                country,
                region,
                street,
                houseNumber,
                zip,
                contactName,
                contactNumber,
                contactEmail
        );
    }

    public static Property createProperty() {
        return Property.builder()
                    .name(name)
                    .starRatings(starRatings)
                    .type(CreatePropertyTypeServiceTest.createPropertyType())
                    .description(description)
                    .country(country)
                    .region(region)
                    .street(street)
                    .houseNumber(houseNumber)
                    .zip(zip)
                    .contactName(contactName)
                    .contactNumber(contactNumber)
                    .contactEmail(contactEmail)
                .build();
    }

    @BeforeEach
    public void setupValidExecution() {
        when(findPropertyByNamePort.findPropertyByName(anyString()))
                .thenReturn(null);

        when(countryValidatorPort.isCountryValid(anyString()))
                .thenReturn(true);

        PropertyType propertyType = CreatePropertyTypeServiceTest.createPropertyType();
        propertyType.setId(propertyTypeId);
        when(findPropertyTypeByIdPort.findById(anyLong()))
                .thenReturn(propertyType);

        Property property = createProperty();
        property.setType(propertyType);
        when(useCaseMapper.toDomainEntity(any(CreatePropertyUseCase.Command.class)))
                .thenReturn(property);

        final long id = 13L;
        when(persistPropertyPort.saveProperty(any(Property.class)))
                .thenAnswer((invocation -> {
                    Property propertyArg = invocation.getArgument(0, Property.class);
                    propertyArg.setId(id);
                    return propertyArg;
                }));
    }

    @Test
    public void createProperty_whenThereIsAlreadyPropertyWithTheGivenName_shouldThrowException() {
        Property property = createProperty();
        when(findPropertyByNamePort.findPropertyByName(anyString()))
                .thenReturn(property);

        CreatePropertyUseCase.Command command = createCommand();

        assertThrows(
                CreatePropertyUseCase.PropertyInvalidCommandException.class,
                () -> createPropertyUseCase.createProperty(command)
        );
    }

    @Test
    public void createProperty_whenThereIsNotPropertyWithTheGivenName_shouldNotThrowException() {
        when(findPropertyByNamePort.findPropertyByName(anyString()))
                .thenReturn(null);

        CreatePropertyUseCase.Command command = createCommand();

        assertDoesNotThrow(
                () -> createPropertyUseCase.createProperty(command)
        );
    }

    @Test
    public void createProperty_whenTheGivenCountryDoesNotExist_shouldThrowException() {
        when(countryValidatorPort.isCountryValid(anyString()))
                .thenReturn(false);

        CreatePropertyUseCase.Command command = createCommand();

        assertThrows(
                CreatePropertyUseCase.PropertyInvalidCommandException.class,
                () -> createPropertyUseCase.createProperty(command)
        );
    }

    @Test
    public void createProperty_whenTheGivenCountryExists_shouldNotThrowException() {
        when(countryValidatorPort.isCountryValid(anyString()))
                .thenReturn(true);

        CreatePropertyUseCase.Command command = createCommand();

        assertDoesNotThrow(
                () -> createPropertyUseCase.createProperty(command)
        );
    }

    @Test
    public void createProperty_whenPropertyTypeWithTheGivenIdDoesNotExist_shouldThrowException() {
        when(findPropertyTypeByIdPort.findById(anyLong()))
                .thenReturn(null);

        CreatePropertyUseCase.Command command = createCommand();

        assertThrows(
                CreatePropertyUseCase.PropertyInvalidCommandException.class,
                () -> createPropertyUseCase.createProperty(command)
        );
    }

    @Test
    public void createProperty_whenPropertyTypeWithTheGivenIdExists_shouldNotThrowException() {
        when(findPropertyTypeByIdPort.findById(anyLong()))
                .thenReturn(CreatePropertyTypeServiceTest.createPropertyType());

        CreatePropertyUseCase.Command command = createCommand();

        assertDoesNotThrow(
                () -> createPropertyUseCase.createProperty(command)
        );
    }

    @Test
    public void createProperty_whenAllArgumentsAreValid_shouldCreateANewPropertyWithId() {
        final long id = 13L;
        when(persistPropertyPort.saveProperty(any(Property.class)))
                .thenAnswer((invocation -> {
                    Property propertyArg = invocation.getArgument(0, Property.class);
                    propertyArg.setId(id);
                    return propertyArg;
                }));

        CreatePropertyUseCase.Command command = createCommand();

        Property actualResult = createPropertyUseCase.createProperty(command);

        assertAll(
                () -> assertEquals(id, actualResult.getId()),
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
}
