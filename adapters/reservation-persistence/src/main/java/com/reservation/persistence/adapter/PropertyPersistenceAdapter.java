package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.port.out.FindPropertyByNamePort;
import com.reservation.application.port.out.PersistPropertyPort;
import com.reservation.common.component.PersistenceAdapter;
import com.reservation.persistence.core.repository.PropertyJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
class PropertyPersistenceAdapter implements FindPropertyByNamePort, PersistPropertyPort {

    private final PropertyJpaRepository propertyJpaRepository;

    @Override
    public Property findPropertyByName(String name) {
        log.info("Fetching property by name: {}", name);
        return propertyJpaRepository.findByName(name);
    }

    @Override
    public Property saveProperty(Property property) {
        log.info("Persisting property: {}", property);
        return propertyJpaRepository.saveAndFlush(property);
    }
}
