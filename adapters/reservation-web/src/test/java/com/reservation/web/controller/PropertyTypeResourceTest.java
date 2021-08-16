package com.reservation.web.controller;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.application.service.CreatePropertyTypeServiceTest;
import com.reservation.web.mapper.PropertyTypeApiMapper;
import com.reservation.web.model.PropertyTypeApi;
import com.reservation.web.model.error.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PropertyTypeResource.class)
public class PropertyTypeResourceTest {

    @Autowired
    private PropertyTypeResource propertyTypeResource;

    @MockBean
    private PropertyTypeApiMapper propertyTypeApiMapper;

    @MockBean
    private CreatePropertyTypeUseCase createPropertyTypeUseCase;

    private final static Long ID = 3L;

    private final static String NAME = "Hotel";

    private final static boolean ALLOWS_MULTIPLE_ROOMS = true;

    private final static boolean REQUIRES_ALTERNATIVE = false;

    private final static boolean REQUIRES_PRIVATE = true;

    public static PropertyTypeApi createPropertyTypeApi() {
        return PropertyTypeApi.builder()
                    .name(NAME)
                    .allowsMultipleRooms(ALLOWS_MULTIPLE_ROOMS)
                    .requiresPrivate(REQUIRES_PRIVATE)
                    .requiresAlternative(REQUIRES_ALTERNATIVE)
                .build();
    }

    @Test
    public void createPropertyType_whenTheRequestIsValid_shouldReturnStatusCodeCreated() throws Exception {

        PropertyType entity = CreatePropertyTypeServiceTest.createPropertyType();

        PropertyTypeApi apiModelRequest = PropertyTypeResourceTest.createPropertyTypeApi();
        PropertyTypeApi apiModelResponse = PropertyTypeResourceTest.createPropertyTypeApi();
        apiModelResponse.setId(ID);

        CreatePropertyTypeUseCase.Command command =
                new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE);

        when(propertyTypeApiMapper.toCreatePropertyTypeCommand(apiModelRequest))
                .thenReturn(command);
        when(propertyTypeApiMapper.toApiModel(entity))
                .thenReturn(apiModelResponse);
        when(createPropertyTypeUseCase.createPropertyType(command))
                .thenReturn(entity);

        ResponseEntity<PropertyTypeApi> response = propertyTypeResource.createPropertyType(apiModelRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.LOCATION));
        PropertyTypeApi responseBody = response.getBody();
        assertAll(
                () -> assertNotNull(responseBody),
                () -> assertEquals(ID, responseBody.getId()),
                () -> assertEquals(apiModelRequest.getName(), responseBody.getName()),
                () -> assertEquals(apiModelRequest.getRequiresPrivate(), responseBody.getRequiresPrivate()),
                () -> assertEquals(apiModelRequest.getAllowsMultipleRooms(), responseBody.getAllowsMultipleRooms()),
                () -> assertEquals(apiModelRequest.getRequiresAlternative(), responseBody.getRequiresAlternative())
        );
    }

    @Test
    public void createPropertyType_whenTheRequestIdIsNotNull_shouldThrowException() throws Exception {

        PropertyTypeApi apiModelRequest = PropertyTypeResourceTest.createPropertyTypeApi();
        apiModelRequest.setId(ID);

        assertThrows(
                InvalidRequestException.class,
                () -> propertyTypeResource.createPropertyType(apiModelRequest)
        );
    }

    @Test
    public void createPropertyType_whenApplicationModuleThrowsException_shouldThrowException() throws Exception {

        PropertyTypeApi apiModelRequest = PropertyTypeResourceTest.createPropertyTypeApi();

        CreatePropertyTypeUseCase.Command command =
                new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE);

        when(propertyTypeApiMapper.toCreatePropertyTypeCommand(apiModelRequest))
                .thenReturn(command);
        when(createPropertyTypeUseCase.createPropertyType(command))
                .thenThrow(CreatePropertyTypeUseCase.NonUniquePropertyTypeNameException.class);

        assertThrows(
                CreatePropertyTypeUseCase.NonUniquePropertyTypeNameException.class,
                () -> propertyTypeResource.createPropertyType(apiModelRequest)
        );
    }
}
