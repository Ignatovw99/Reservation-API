package com.reservation.application.service;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.in.CreatePropertyUseCase;
import com.reservation.application.port.out.CountryValidatorPort;
import com.reservation.application.port.out.FindPropertyByNamePort;
import com.reservation.application.port.out.FindPropertyTypeByIdPort;
import com.reservation.application.port.out.PersistPropertyPort;
import com.reservation.common.component.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class CreatePropertyService implements CreatePropertyUseCase {

    private final FindPropertyByNamePort findPropertyByNamePort;

    private final CountryValidatorPort countryValidatorPort;

    private final FindPropertyTypeByIdPort findPropertyTypeByIdPort;

    private final PersistPropertyPort persistPropertyPort;

    private final UseCaseMapper mapper;

    @Override
    public Property createProperty(Command propertyCommand) {
        log.info("Creating a new property: {}", propertyCommand);

        requireUniqueName(propertyCommand.getName());
        requireValidCountry(propertyCommand.getCountry());
        requireExistingPropertyType(propertyCommand.getPropertyTypeId());

        Property property = mapper.toDomainEntity(propertyCommand);
        return persistPropertyPort.saveProperty(property);
    }

    private void requireUniqueName(String name) {
        Property property = findPropertyByNamePort.findPropertyByName(name);
        if (Objects.nonNull(property)) {
            log.error("Creating property failed, property with name \"{} \" already exists", name);
            throw new PropertyInvalidCommandException.Conflict(
                    String.format("Property with name %s already exists", name)
            );
        }
    }

    private void requireValidCountry(String country) {
        boolean isValid = countryValidatorPort.isCountryValid(country);
        if (!isValid) {
            log.error("Creating property failed, country \"{}\" is not valid", country);
            throw new PropertyInvalidCommandException.BadRequest(
                    String.format("Country %s is not valid", country)
            );
        }
    }

    private void requireExistingPropertyType(Long propertyTypeId) {
        PropertyType propertyType = findPropertyTypeByIdPort.findById(propertyTypeId);
        if (Objects.isNull(propertyType)) {
            log.error("Creating property failed, property type with id {} does not exist", propertyTypeId);
            throw new PropertyInvalidCommandException.BadRequest(
                    String.format("Property type with id %d cannot be assigned to the property, because it does not exist", propertyTypeId)
            );
        }
    }
}
