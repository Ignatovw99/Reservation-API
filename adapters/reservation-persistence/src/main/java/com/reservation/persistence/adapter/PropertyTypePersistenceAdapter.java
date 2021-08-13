package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.out.FindPropertyTypeByIdPort;
import com.reservation.application.port.out.FindPropertyTypeByNamePort;
import com.reservation.application.port.out.PersistPropertyTypePort;
import com.reservation.common.component.PersistenceAdapter;
import com.reservation.persistence.core.repository.PropertyTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
class PropertyTypePersistenceAdapter implements
        PersistPropertyTypePort,
        FindPropertyTypeByNamePort,
        FindPropertyTypeByIdPort {

    private final PropertyTypeJpaRepository propertyTypeJpaRepository;

    @Override
    public PropertyType savePropertyType(PropertyType propertyType) {
        log.info("Persisting Property type: {}", propertyType);
        return propertyTypeJpaRepository.save(propertyType);
    }

    @Override
    public PropertyType findPropertyTypeByName(String name) {
        log.info("Fetching Property type by name: {}", name);
        return propertyTypeJpaRepository.findByName(name);
    }

    @Override
    public PropertyType findById(Long id) {
        log.info("Fetching Property type by id: {}", id);
        return propertyTypeJpaRepository.findById(id)
                .orElse(null);
    }
}
