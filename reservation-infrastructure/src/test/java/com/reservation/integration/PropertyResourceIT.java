package com.reservation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.reservation.application.domain.entity.Property;
import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.service.CreatePropertyServiceTest;
import com.reservation.application.service.CreatePropertyTypeServiceTest;
import com.reservation.persistence.core.repository.PropertyJpaRepository;
import com.reservation.persistence.core.repository.PropertyTypeJpaRepository;
import com.reservation.web.model.PropertyApi;
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

import static com.reservation.application.service.CreatePropertyServiceTest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PropertyResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyTypeJpaRepository propertyTypeRepository;

    @Autowired
    private PropertyJpaRepository propertyRepository;

    public static PropertyApi createPropertyApi(Long propertyTypeId) {
        return PropertyApi.builder()
                .name(name)
                .starRatings(starRatings)
                .propertyTypeId(propertyTypeId)
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
    @Transactional
    public void createProperty_whenRequestHasNonNullId_shouldReturnBadRequest() throws Exception {
        PropertyApi propertyApi = createPropertyApi(32L);
        propertyApi.setId(3L);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyApi));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createProperty_whenThereIsAlreadyPropertyWithTheGivenName_shouldReturnStatusCodeConflict() throws Exception {

        PropertyApi propertyApi = PropertyResourceIT.createPropertyApi(32L);

        Property property = CreatePropertyServiceTest.createProperty();
        property.setName(propertyApi.getName());

        PropertyType type = property.getType();
        propertyTypeRepository.saveAndFlush(type);
        propertyRepository.saveAndFlush(property);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyApi));

        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        TODO: add error property assertions
    }

    @Test
    @Transactional
    public void createProperty_whenTheGivenCountryIsNotValid_shouldReturnStatusCodeBadRequest() throws Exception {

        PropertyType type = CreatePropertyTypeServiceTest.createPropertyType();
        propertyTypeRepository.saveAndFlush(type);

        PropertyApi propertyApi = PropertyResourceIT.createPropertyApi(type.getId());
        propertyApi.setCountry("Invalid");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyApi));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        TODO: add error property assertions
    }

    @Test
    @Transactional
    public void createProperty_whenPropertyTypeDoesNotExist_shouldReturnStatusCodeBadRequest() throws Exception {

        PropertyType type = CreatePropertyTypeServiceTest.createPropertyType();
        propertyTypeRepository.saveAndFlush(type);

        long invalidPropertyTypeId = type.getId() + 4;
        PropertyApi propertyApi = PropertyResourceIT.createPropertyApi(invalidPropertyTypeId);
        propertyApi.setCountry("Greece");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyApi));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        TODO: add error property assertions
    }

    @Test
    @Transactional
    public void createProperty_whenRequestPropertyIsValid_shouldReturnPropertyWithStatusCodeCreated() throws Exception {

        PropertyType type = CreatePropertyTypeServiceTest.createPropertyType();
        propertyTypeRepository.saveAndFlush(type);

        PropertyApi propertyApi = PropertyResourceIT.createPropertyApi(type.getId());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(propertyApi));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, startsWith("/api/properties/")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.starRatings", is(starRatings.intValue())))
                .andExpect(jsonPath("$.propertyTypeId", is(type.getId().intValue())))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.country", is(country)))
                .andExpect(jsonPath("$.region", is(region)))
                .andExpect(jsonPath("$.street", is(street)))
                .andExpect(jsonPath("$.houseNumber", is(houseNumber)))
                .andExpect(jsonPath("$.zip", is(zip)))
                .andExpect(jsonPath("$.contactName", is(contactName)))
                .andExpect(jsonPath("$.contactNumber", is(contactNumber)))
                .andExpect(jsonPath("$.contactEmail", is(contactEmail)))
                .andDo(result -> {
                    Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
                    boolean actualResult = propertyRepository.existsById(Long.valueOf(id));
                    assertTrue(actualResult);
                });
    }
}
