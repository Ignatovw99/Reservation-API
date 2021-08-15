package com.reservation.web.mapper;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.port.in.CreatePropertyUseCase;
import com.reservation.common.contract.DomainEntityApiMapper;
import com.reservation.web.model.PropertyApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyApiMapper extends DomainEntityApiMapper<Property, PropertyApi> {

    CreatePropertyUseCase.Command toCreatePropertyCommand(PropertyApi propertyApi);

    @Override
    @Mapping(source = "type.id", target = "propertyTypeId")
    PropertyApi toApiModel(Property domainEntity);
}
