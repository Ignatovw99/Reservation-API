package com.reservation.web.mapper;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.common.contract.DomainEntityAPIMapper;
import com.reservation.web.model.PropertyTypeAPI;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyTypeAPIMapper extends DomainEntityAPIMapper<PropertyType, PropertyTypeAPI> {

    CreatePropertyTypeUseCase.Command toCreatePropertyTypeCommand(PropertyTypeAPI propertyTypeAPI);
}
