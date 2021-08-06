package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.out.FindPropertyTypeByNamePort;
import com.reservation.application.port.out.PersistPropertyTypePort;
import com.reservation.common.component.PersistenceAdapter;
import com.reservation.persistence.core.repository.PropertyTypeJpaRepository;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class PropertyTypePersistenceAdapter implements PersistPropertyTypePort, FindPropertyTypeByNamePort {

    private final PropertyTypeJpaRepository propertyTypeJpaRepository;

    @Override
    public PropertyType savePropertyType(PropertyType propertyType) {
        return propertyTypeJpaRepository.save(propertyType);
    }

    @Override
    public PropertyType findPropertyTypeByName(String name) {
        return propertyTypeJpaRepository.findByName(name);
    }
}
