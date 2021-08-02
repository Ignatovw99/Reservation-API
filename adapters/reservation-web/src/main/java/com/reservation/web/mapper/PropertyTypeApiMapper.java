package com.reservation.web.mapper;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.common.contract.DomainEntityApiMapper;
import com.reservation.web.model.PropertyTypeApi;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyTypeApiMapper extends DomainEntityApiMapper<PropertyType, PropertyTypeApi> {

    CreatePropertyTypeUseCase.Command toCreatePropertyTypeCommand(PropertyTypeApi propertyTypeAPI);
}
