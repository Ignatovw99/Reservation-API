package com.reservation.web.controller;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.common.component.WebAdapter;
import com.reservation.web.mapper.PropertyTypeApiMapper;
import com.reservation.web.model.PropertyTypeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter(valueURL = "/api/property-type")
class PropertyTypeResource {

    private final CreatePropertyTypeUseCase createPropertyTypeUseCase;

    private final PropertyTypeApiMapper mapper;

    @Autowired
    public PropertyTypeResource(CreatePropertyTypeUseCase createPropertyTypeUseCase, PropertyTypeApiMapper mapper) {
        this.createPropertyTypeUseCase = createPropertyTypeUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PropertyTypeApi> createPropertyType(@RequestBody PropertyTypeApi propertyTypeAPI) {

        CreatePropertyTypeUseCase.Command command = mapper.toCreatePropertyTypeCommand(propertyTypeAPI);
        PropertyType createdPropertyType = createPropertyTypeUseCase.createPropertyType(command);
        PropertyTypeApi result = mapper.toApiModel(createdPropertyType);

        return ApiController.post(result);
    }
}
