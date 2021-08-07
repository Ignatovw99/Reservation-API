package com.reservation.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.web.config.WebConfig;
import com.reservation.web.mapper.PropertyTypeApiMapper;
import com.reservation.web.model.PropertyTypeApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyTypeResource.class)
@ContextConfiguration(classes = WebConfig.class)
public class PropertyTypeResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    public static PropertyType createPropertyType() {
        return PropertyType.builder()
                    .id(ID)
                    .name(NAME)
                    .allowsMultipleRooms(ALLOWS_MULTIPLE_ROOMS)
                    .requiresPrivate(REQUIRES_PRIVATE)
                    .requiresAlternative(REQUIRES_ALTERNATIVE)
                .build();
    }

    @Test
    public void createPropertyType_whenTheRequestIsValid_shouldReturnStatusCodeCreated() throws Exception {

        PropertyType entity = PropertyTypeResourceTest.createPropertyType();
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

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/property-type")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apiModelRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, startsWith("/api/property-type/")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.requiresPrivate", is(REQUIRES_PRIVATE)))
                .andExpect(jsonPath("$.allowsMultipleRooms", is(ALLOWS_MULTIPLE_ROOMS)))
                .andExpect(jsonPath("$.requiresAlternative", is(REQUIRES_ALTERNATIVE)));
    }

    @Test
    public void createPropertyType_whenTheRequestIdIsNotNull_shouldReturnBadRequest() throws Exception {

        PropertyTypeApi apiModelRequest = PropertyTypeResourceTest.createPropertyTypeApi();
        apiModelRequest.setId(ID);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/property-type")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apiModelRequest));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createPropertyType_whenApplicationModuleThrowsException_shouldReturnStatusCodeConflict() throws Exception {

        PropertyTypeApi apiModelRequest = PropertyTypeResourceTest.createPropertyTypeApi();

        CreatePropertyTypeUseCase.Command command =
                new CreatePropertyTypeUseCase.Command(NAME, REQUIRES_PRIVATE, ALLOWS_MULTIPLE_ROOMS, REQUIRES_ALTERNATIVE);

        when(propertyTypeApiMapper.toCreatePropertyTypeCommand(apiModelRequest))
                .thenReturn(command);
        when(createPropertyTypeUseCase.createPropertyType(command))
                .thenThrow(CreatePropertyTypeUseCase.NonUniquePropertyTypeNameException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/property-type")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apiModelRequest));

        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
