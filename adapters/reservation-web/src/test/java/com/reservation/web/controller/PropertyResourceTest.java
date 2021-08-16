package com.reservation.web.controller;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyUseCase;
import com.reservation.application.service.CreatePropertyServiceTest;
import com.reservation.application.service.CreatePropertyTypeServiceTest;
import com.reservation.web.mapper.PropertyApiMapper;
import com.reservation.web.model.PropertyApi;
import com.reservation.web.model.error.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.reservation.application.service.CreatePropertyServiceTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PropertyResource.class)
public class PropertyResourceTest {

    @Autowired
    private PropertyResource propertyResource;

    @MockBean
    private CreatePropertyUseCase createPropertyUseCase;

    @MockBean
    private PropertyApiMapper propertyApiMapper;

    public static PropertyApi createPropertyApi() {
        PropertyType propertyType = CreatePropertyTypeServiceTest.createPropertyType();
        propertyType.setId(123L);
        return PropertyApi.builder()
                    .name(name)
                    .starRatings(starRatings)
                    .propertyTypeId(321L)
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

    @Test
    public void createProperty_whenTheRequestIsValid_shouldReturnStatusCodeCreated() throws Exception {
        Property entity = CreatePropertyServiceTest.createProperty();

        PropertyApi apiModelRequest = PropertyResourceTest.createPropertyApi();
        PropertyApi apiModelResponse = PropertyResourceTest.createPropertyApi();
        apiModelResponse.setId(15L);

        CreatePropertyUseCase.Command command = createCommand();

        when(propertyApiMapper.toCreatePropertyCommand(apiModelRequest))
                .thenReturn(command);
        when(propertyApiMapper.toApiModel(entity))
                .thenReturn(apiModelResponse);
        when(createPropertyUseCase.createProperty(command))
                .thenReturn(entity);

        ResponseEntity<PropertyApi> responseActual = propertyResource.createProperty(apiModelRequest);

        assertEquals(HttpStatus.CREATED, responseActual.getStatusCode());
        assertTrue(responseActual.getHeaders().containsKey(HttpHeaders.LOCATION));
        PropertyApi apiResponse = responseActual.getBody();
        assertAll(
                () -> assertNotNull(apiResponse),
                () -> assertEquals(15L, apiResponse.getId()),
                () -> assertEquals(apiModelRequest.getName(), apiResponse.getName()),
                () -> assertEquals(apiModelRequest.getPropertyTypeId(), apiResponse.getPropertyTypeId()),
                () -> assertEquals(apiModelRequest.getDescription(), apiResponse.getDescription()),
                () -> assertEquals(apiModelRequest.getCountry(), apiResponse.getCountry()),
                () -> assertEquals(apiModelRequest.getStarRatings(), apiResponse.getStarRatings()),
                () -> assertEquals(apiModelRequest.getContactName(), apiResponse.getContactName()),
                () -> assertEquals(apiModelRequest.getContactNumber(), apiResponse.getContactNumber()),
                () -> assertEquals(apiModelRequest.getContactEmail(), apiResponse.getContactEmail())
        );
    }

    @Test
    public void createProperty_whenTheRequestIdIsNotNull_shouldThrowException() throws Exception {

        PropertyApi apiModelRequest = PropertyResourceTest.createPropertyApi();
        apiModelRequest.setId(31L);

        assertThrows(
                InvalidRequestException.class,
                () -> propertyResource.createProperty(apiModelRequest)
        );
    }

    @Test
    public void createProperty_whenSomethingIsInvalid_shouldThrowException() throws Exception {

        PropertyApi apiModelRequest = PropertyResourceTest.createPropertyApi();

        CreatePropertyUseCase.Command command = createCommand();

        when(propertyApiMapper.toCreatePropertyCommand(apiModelRequest))
                .thenReturn(command);
        when(createPropertyUseCase.createProperty(command))
                .thenThrow(CreatePropertyUseCase.PropertyInvalidCommandException.class);

        assertThrows(
                CreatePropertyUseCase.PropertyInvalidCommandException.class,
                () -> propertyResource.createProperty(apiModelRequest)
        );
    }
}
