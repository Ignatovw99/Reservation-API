package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.PropertyType;
import com.reservation.persistence.PersistenceContextTest;
import com.reservation.persistence.core.repository.PropertyTypeJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceContextTest
class PropertyTypePersistenceAdapterTest {

    @Autowired
    private PropertyTypePersistenceAdapter propertyTypePersistenceAdapter;

    @Autowired
    private PropertyTypeJpaRepository propertyTypeJpaRepository;

    @Test
    public void findPropertyTypeByName_WhenPropertyTypeWithGivenNameExists_shouldReturnIt() {
        final String name = "Hotel";

        PropertyType propertyType = PropertyType.builder()
                    .name(name)
                    .requiresPrivate(true)
                    .allowsMultipleRooms(false)
                    .requiresAlternative(true)
                .build();
        propertyTypeJpaRepository.saveAndFlush(propertyType);

        PropertyType actualType = propertyTypePersistenceAdapter.findPropertyTypeByName(name);

        assertNotNull(actualType);
        assertEquals(propertyType.getId(), actualType.getId());
    }

    @Test
    public void findPropertyTypeByName_WhenPropertyTypeWithGivenNameDoesNotExist_shouldReturnNull() {
        final String name = "Hotel";

        long actualCount = propertyTypeJpaRepository.count();
        assertEquals(0, actualCount);

        PropertyType expectedType = propertyTypePersistenceAdapter.findPropertyTypeByName(name);
        assertNull(expectedType);
    }

    @Test
    public void savePropertyType_WhenThereIsAlreadyTypeWithTheGivenName_shouldThrowException() {
        final String name = "Hotel";

        PropertyType propertyType = PropertyType.builder()
                    .name(name)
                    .requiresPrivate(true)
                    .allowsMultipleRooms(false)
                    .requiresAlternative(true)
                .build();
        propertyTypeJpaRepository.saveAndFlush(propertyType);

        PropertyType propertyType2 = PropertyType.builder()
                .name(name)
                .requiresPrivate(true)
                .allowsMultipleRooms(false)
                .requiresAlternative(true)
                .build();
        assertThrows(
                DataIntegrityViolationException.class,
                () -> propertyTypePersistenceAdapter.savePropertyType(propertyType2)
        );
    }

    @Test
    public void savePropertyType_WhenThereIsNotTypeWithTheGivenName_shouldPersistIt() {
        final String name = "Hotel";

        PropertyType propertyType = PropertyType.builder()
                    .name(name)
                    .requiresPrivate(true)
                    .allowsMultipleRooms(false)
                    .requiresAlternative(true)
                .build();
        assertNull(propertyType.getId());

        PropertyType savedPropertyType = propertyTypePersistenceAdapter.savePropertyType(propertyType);
        assertNotNull(propertyType.getId());

        boolean actual = propertyTypeJpaRepository.existsById(savedPropertyType.getId());
        assertTrue(actual);
    }

    @Test
    public void savePropertyType_whenBooleanVariablesAreNull_shouldThrowException() {
        final String name = "Hotel";

        PropertyType propertyType = new PropertyType();
        propertyType.setName(name);

        assertAll(
                () -> assertNull(propertyType.getRequiresPrivate()),
                () -> assertNull(propertyType.getAllowsMultipleRooms()),
                () -> assertNull(propertyType.getRequiresAlternative())
        );

        assertThrows(
                DataIntegrityViolationException.class,
                () -> propertyTypePersistenceAdapter.savePropertyType(propertyType)
        );
    }
}
