package com.reservation.web.controller;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.common.component.WebAdapter;
import com.reservation.web.mapper.PropertyTypeApiMapper;
import com.reservation.web.model.PropertyTypeApi;
import com.reservation.web.model.error.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@WebAdapter(valueURL = "/api/property-type")
@RequiredArgsConstructor
@Slf4j
class PropertyTypeResource {

    private final CreatePropertyTypeUseCase createPropertyTypeUseCase;

    private final PropertyTypeApiMapper mapper;

    @PostMapping
    public ResponseEntity<PropertyTypeApi> createPropertyType(@RequestBody PropertyTypeApi propertyTypeApi) {
        log.info("REST Http request: create property type: {}", propertyTypeApi);

        if (Objects.nonNull(propertyTypeApi.getId())) {
            log.error("Request failed, request model should not have id");
            throw new InvalidRequestException("ID must be null!");
        }

        CreatePropertyTypeUseCase.Command command = mapper.toCreatePropertyTypeCommand(propertyTypeApi);
        PropertyType createdPropertyType = createPropertyTypeUseCase.createPropertyType(command);
        PropertyTypeApi result = mapper.toApiModel(createdPropertyType);

        return ApiController.postResponse(result);
    }
}
