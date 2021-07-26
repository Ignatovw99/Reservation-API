package com.reservation.application.service;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.application.port.out.FindPropertyTypeByNamePort;
import com.reservation.application.port.out.PersistPropertyTypePort;
import com.reservation.common.component.UseCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@UseCase
class CreatePropertyTypeService implements CreatePropertyTypeUseCase {

    private final PersistPropertyTypePort savePropertyTypePort;

    private final FindPropertyTypeByNamePort findPropertyTypeByNamePort;

    private final UseCaseMapper mapper;

    @Autowired
    public CreatePropertyTypeService(PersistPropertyTypePort savePropertyTypePort,
                                     FindPropertyTypeByNamePort findPropertyTypeByNamePort,
                                     UseCaseMapper mapper) {

        this.savePropertyTypePort = savePropertyTypePort;
        this.findPropertyTypeByNamePort = findPropertyTypeByNamePort;
        this.mapper = mapper;
    }

    @Override
    public PropertyType createPropertyType(Command propertyTypeCommand) throws NonUniquePropertyTypeNameException {
        requireUniqueName(propertyTypeCommand.getName());
        PropertyType propertyType = mapper.toDomainEntity(propertyTypeCommand);
        return savePropertyTypePort.savePropertyType(propertyType);
    }

    private boolean existsByName(String name) {
        PropertyType propertyType = findPropertyTypeByNamePort.findPropertyTypeByName(name);
        return Objects.nonNull(propertyType);
    }

    private void requireUniqueName(String name) throws NonUniquePropertyTypeNameException {
        if (existsByName(name)) {
            throw new NonUniquePropertyTypeNameException(name);
        }
    }
}
