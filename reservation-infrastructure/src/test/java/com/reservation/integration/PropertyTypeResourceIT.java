package com.reservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.reservation.application.domain.entity.PropertyType;
import com.reservation.persistence.core.repository.PropertyTypeJpaRepository;
import com.reservation.web.model.PropertyTypeApi;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PropertyTypeResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropertyTypeJpaRepository propertyTypeRepository;

    @Autowired
    private ObjectMapper mapper;

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
                    .name(NAME)
                    .allowsMultipleRooms(ALLOWS_MULTIPLE_ROOMS)
                    .requiresPrivate(REQUIRES_PRIVATE)
                    .requiresAlternative(REQUIRES_ALTERNATIVE)
                .build();
    }

    @Disabled
    @Test
    @Transactional
    public void createPropertyType_whenTheRequestIsValid_shouldReturnStatusCodeCreated() throws Exception {

        PropertyTypeApi apiModelRequest = PropertyTypeResourceIT.createPropertyTypeApi();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/property-type")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(apiModelRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, startsWith("/api/property-type/")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.requiresPrivate", is(REQUIRES_PRIVATE)))
                .andExpect(jsonPath("$.allowsMultipleRooms", is(ALLOWS_MULTIPLE_ROOMS)))
                .andExpect(jsonPath("$.requiresAlternative", is(REQUIRES_ALTERNATIVE)))
                .andDo(result -> {
                    Long id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
                    boolean actualResult = propertyTypeRepository.existsById(id);
                    assertTrue(actualResult);
                });
    }

    @Disabled
    @Test
    @Transactional
    public void createPropertyType_whenTheRequestContainsId_shouldReturnStatusCodeBadRequest() throws Exception {

        PropertyTypeApi apiModelRequest = PropertyTypeResourceIT.createPropertyTypeApi();
        apiModelRequest.setId(3L);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/property-type")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(apiModelRequest));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Disabled
    @Test
    @Transactional
    public void createPropertyType_whenThereIsAlreadyPropertyTypeWithTheGivenName_shouldReturnStatusCodeConflict() throws Exception {

        PropertyTypeApi apiModelRequest = PropertyTypeResourceIT.createPropertyTypeApi();

        PropertyType entity = PropertyTypeResourceIT.createPropertyType();
        entity.setName(apiModelRequest.getName());
        propertyTypeRepository.saveAndFlush(entity);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/property-type")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(apiModelRequest));

        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        TODO: add error property assertions
    }
}
