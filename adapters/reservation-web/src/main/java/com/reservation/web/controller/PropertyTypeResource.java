package com.reservation.web.controller;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.common.component.WebAdapter;
import com.reservation.web.mapper.PropertyTypeAPIMapper;
import com.reservation.web.model.PropertyTypeAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter(valueURL = "/api/property-type")
class PropertyTypeResource {

    private final CreatePropertyTypeUseCase createPropertyTypeUseCase;

    private final PropertyTypeAPIMapper mapper;

    @Autowired
    public PropertyTypeResource(CreatePropertyTypeUseCase createPropertyTypeUseCase, PropertyTypeAPIMapper mapper) {
        this.createPropertyTypeUseCase = createPropertyTypeUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PropertyTypeAPI> createPropertyType(@RequestBody PropertyTypeAPI propertyTypeAPI) {

        CreatePropertyTypeUseCase.Command command = mapper.toCreatePropertyTypeCommand(propertyTypeAPI);
        PropertyType createdPropertyType = createPropertyTypeUseCase.createPropertyType(command);
        PropertyTypeAPI result = mapper.toApiModel(createdPropertyType);

        return APIController
                .post(result, "/api/property-type/%d", result.getId());
    }
}
