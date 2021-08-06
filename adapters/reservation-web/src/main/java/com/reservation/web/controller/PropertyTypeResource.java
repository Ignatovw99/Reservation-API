package com.reservation.web.controller;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.common.component.WebAdapter;
import com.reservation.web.mapper.PropertyTypeApiMapper;
import com.reservation.web.model.PropertyTypeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter(valueURL = "/api/property-type")
@RequiredArgsConstructor
class PropertyTypeResource {

    private final CreatePropertyTypeUseCase createPropertyTypeUseCase;

    private final PropertyTypeApiMapper mapper;

    @PostMapping
    public ResponseEntity<PropertyTypeApi> createPropertyType(@RequestBody PropertyTypeApi propertyTypeAPI) {

        CreatePropertyTypeUseCase.Command command = mapper.toCreatePropertyTypeCommand(propertyTypeAPI);
        PropertyType createdPropertyType = createPropertyTypeUseCase.createPropertyType(command);
        PropertyTypeApi result = mapper.toApiModel(createdPropertyType);

        return ApiController.postResponse(result);
    }
}
