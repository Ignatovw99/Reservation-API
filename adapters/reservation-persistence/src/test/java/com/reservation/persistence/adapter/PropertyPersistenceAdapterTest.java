package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.Property;
import com.reservation.application.domain.entity.PropertyType;
import com.reservation.application.port.out.FindPropertyByNamePort;
import com.reservation.application.port.out.PersistPropertyPort;
import com.reservation.application.service.CreatePropertyServiceTest;
import com.reservation.persistence.PersistenceContextTest;
import com.reservation.persistence.core.repository.PropertyJpaRepository;
import com.reservation.persistence.core.repository.PropertyTypeJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceContextTest
public class PropertyPersistenceAdapterTest {

    @Autowired
    private FindPropertyByNamePort findPropertyByNamePort;

    @Autowired
    private PersistPropertyPort persistPropertyPort;

    @Autowired
    private PropertyJpaRepository propertyRepository;

    @Autowired
    private PropertyTypeJpaRepository propertyTypeRepository;

    @Test
    public void findPropertyByName_whenPropertyWithTheGivenNameDoesNotExist_shouldReturnNull() {
        Property property = CreatePropertyServiceTest.createProperty();
        PropertyType propertyType = property.getType();

        propertyTypeRepository.saveAndFlush(propertyType);
        propertyRepository.saveAndFlush(property);

        String name = property.getName() + "ASD";
        Property actual = findPropertyByNamePort.findPropertyByName(name);

        assertNull(actual);
    }

    @Test
    public void findPropertyByName_whenPropertyWithTheGivenNameExists_shouldReturnIt() {
        Property property = CreatePropertyServiceTest.createProperty();
        PropertyType propertyType = property.getType();

        propertyTypeRepository.saveAndFlush(propertyType);
        property = propertyRepository.saveAndFlush(property);

        String name = property.getName();
        Property actual = findPropertyByNamePort.findPropertyByName(name);

        assertNotNull(actual);
        assertEquals(property.getId(), actual.getId());
        assertEquals(name, actual.getName());
        assertEquals(property, actual);
    }

    @Test
    public void saveProperty_WhenThereIsAlreadyPropertyWithTheGivenName_shouldThrowException() {
        Property property = CreatePropertyServiceTest.createProperty();
        PropertyType propertyType = property.getType();

        propertyTypeRepository.saveAndFlush(propertyType);
        property = propertyRepository.saveAndFlush(property);

        Property property2 = CreatePropertyServiceTest.createProperty();

        assertEquals(property.getName(), property2.getName());

        assertThrows(
                DataIntegrityViolationException.class,
                () -> persistPropertyPort.saveProperty(property2)
        );
    }

    @Test
    public void saveProperty_whenThereIsNotPropertyWithTheGivenName_shouldPersistIt() {

        Property property = CreatePropertyServiceTest.createProperty();
        PropertyType propertyType = property.getType();

        propertyTypeRepository.saveAndFlush(propertyType);

        assertNull(property.getId());
        assertNull(propertyRepository.findByName(property.getName()));

        Property savedProperty = persistPropertyPort.saveProperty(property);
        assertNotNull(savedProperty.getId());

        boolean actual = propertyRepository.existsById(savedProperty.getId());
        assertTrue(actual);
    }

    @Test
    public void saveProperty_whenPropertyNameIsNull_shouldThrowException() {
        Property property = CreatePropertyServiceTest.createProperty();
        property.setName(null);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> persistPropertyPort.saveProperty(property)
        );
    }
}
