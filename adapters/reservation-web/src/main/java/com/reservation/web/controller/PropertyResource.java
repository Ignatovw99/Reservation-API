package com.reservation.web.controller;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.port.in.CreatePropertyUseCase;
import com.reservation.common.component.WebAdapter;
import com.reservation.web.mapper.PropertyApiMapper;
import com.reservation.web.model.PropertyApi;
import com.reservation.web.model.error.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@WebAdapter(valueURL = "/api/properties")
@RequiredArgsConstructor
@Slf4j
public class PropertyResource {

    private final CreatePropertyUseCase createPropertyUseCase;

    private final PropertyApiMapper mapper;

    @PostMapping
    public ResponseEntity<PropertyApi> createProperty(@RequestBody PropertyApi propertyApi) {
        log.info("REST Http request: create property: {}", propertyApi);

        if (Objects.nonNull(propertyApi.getId())) {
            log.error("Request failed, request model should not have id");
            throw new InvalidRequestException("ID must be null!");
        }

        CreatePropertyUseCase.Command command = mapper.toCreatePropertyCommand(propertyApi);
        Property createdProperty = createPropertyUseCase.createProperty(command);
        PropertyApi result = mapper.toApiModel(createdProperty);

        return ApiController.postResponse(result);
    }
}
