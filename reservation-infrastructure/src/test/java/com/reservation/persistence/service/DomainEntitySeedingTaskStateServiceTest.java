package com.reservation.persistence.service;

import com.reservation.application.domain.entity.Location;
import com.reservation.application.domain.entity.Property;
import com.reservation.common.processor.DomainEntityProperty;
import com.reservation.persistence.core.entity.DataTypeJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityPropertyJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class DomainEntitySeedingTaskStateServiceTest {

    private DomainEntitySeedingTaskStateService domainEntitySeedingTaskStateService;

    @BeforeEach
    public void setUp() {
        domainEntitySeedingTaskStateService = new DomainEntitySeedingTaskStateService();
    }

    private DomainEntityPropertyJpaEntity initDomainEntityJpaProperty(Class<?> domainEntityClass, Class<?> dataTypeClass, String name) {
        DomainEntityJpaEntity domainEntityJpaEntity = new DomainEntityJpaEntity();
        domainEntityJpaEntity.setName(domainEntityClass.getName());

        DataTypeJpaEntity dataTypeJpaEntity = new DataTypeJpaEntity();
        dataTypeJpaEntity.setFullClassName(dataTypeClass.getName());

        DomainEntityPropertyJpaEntity propertyJpa = new DomainEntityPropertyJpaEntity();
        propertyJpa.setName(name);
        propertyJpa.setDomainEntity(domainEntityJpaEntity);
        propertyJpa.setDataType(dataTypeJpaEntity);
        return propertyJpa;
    }

    @Test
    public void setDomainEntityJpaProperties_shouldAddPropertiesEntities() {
        DomainEntityPropertyJpaEntity propertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, "name");
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(propertyJpaEntity));

        Optional<DomainEntityJpaEntity> result = domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(Property.class);
        assertTrue(result.isPresent());
        assertEquals(Property.class.getName(), result.get().getName());
    }

    @Test
    public void setDomainEntityJpaProperties_shouldAddPropertiesDataTypes() {
        DomainEntityPropertyJpaEntity propertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, "name");
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(propertyJpaEntity));

        Optional<DataTypeJpaEntity> result = domainEntitySeedingTaskStateService.findJpaDataTypeByClass(String.class);
        assertTrue(result.isPresent());
        assertEquals(String.class.getName(), result.get().getFullClassName());
    }

    @Test
    public void allGettersShouldNotReturnNull() {
        assertNotNull(domainEntitySeedingTaskStateService.findJpaDataTypeByClass(String.class));
        assertNotNull(domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(Property.class));

        assertNotNull(domainEntitySeedingTaskStateService.getNewDomainEntityPropertiesOrderedByEntity());
        assertNotNull(domainEntitySeedingTaskStateService.getUnusedDomainJpaEntities());
    }

    @Test
    public void findPropertyByEntityNameAndPropertyName_whenThereIsNotSuchDomainEntity_shouldReturnNull() {
        String propertyName = "name";
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, propertyName);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);

        String domainEntityName = Location.class.getName();

        DomainEntityProperty domainEntityProperty =
                domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, propertyName);

        assertNull(domainEntityProperty);
    }

    @Test
    public void findPropertyByEntityNameAndPropertyName_whenThereIsNotSuchPropertyAtAll_shouldReturnNull() {
        String actualPropertyName = "name";
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, actualPropertyName);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);

        String domainEntityName = Property.class.getName();
        String expectedPropertyName = "stars";

        DomainEntityProperty domainEntityProperty =
                domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, expectedPropertyName);

        assertNull(domainEntityProperty);
    }

    @Test
    public void findPropertyByEntityNameAndPropertyName_whenThereIsSuchAPropertyButItIsNotAssignedToTheExpectedEntity_shouldReturnNull() {
        String propertyName = "name";
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, propertyName);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);

        String domainEntityName = Location.class.getName();

        DomainEntityProperty domainEntityProperty =
                domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, propertyName);

        assertNull(domainEntityProperty);
    }

    @Test
    public void findPropertyByEntityNameAndPropertyName_whenThereIsSuchAPropertyAssignedToTheExpectedEntity_shouldReturnThisProperty() {
        String propertyName = "name";
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, propertyName);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);

        String domainEntityName = Property.class.getName();

        DomainEntityProperty domainEntityProperty =
                domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, propertyName);

        assertNotNull(domainEntityProperty);
    }

    @Test
    public void findPropertyByEntityNameAndPropertyName_whenThereIsSuchAPropertyAssignedToTheExpectedEntity_shouldBeMarkedAsOldProperty() {
        String propertyName = "name";
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, propertyName);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);

        String domainEntityName = Property.class.getName();

        DomainEntityProperty domainEntityProperty =
                domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, propertyName);

        assertFalse(domainEntityProperty.isNew());
    }

    @Test
    public void findPropertyByEntityNameAndPropertyName_whenThereIsSuchAPropertyAssignedToTheExpectedEntity_shouldMarkDomainJpaEntityAsUsedIfExists() {
        String propertyName = "name";
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, propertyName);
        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, propertyName);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(domainEntityPropertyJpaEntity));

        String domainEntityName = Property.class.getName();

        domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, propertyName);

        List<DomainEntityJpaEntity> res = domainEntitySeedingTaskStateService.getUnusedDomainJpaEntities();
        assertThat(res, not(hasItem(domainEntityPropertyJpaEntity.getDomainEntity())));
    }

    @Test
    public void findPropertyByEntityNameAndPropertyName_whenThereIsSuchAPropertyAssignedToTheExpectedEntity_shouldMarkDataJpaTypeOfPropertyAsUsedIfExists() {
        String propertyName = "name";
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, propertyName);
        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, propertyName);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(domainEntityPropertyJpaEntity));

        String domainEntityName = Property.class.getName();

        domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, propertyName);

        List<DataTypeJpaEntity> res = domainEntitySeedingTaskStateService.getUnusedDataJpaTypes();

        assertThat(res, not(hasItem(domainEntityPropertyJpaEntity.getDataType())));
    }

    @Test
    public void findJpaDomainEntityByClass_whenDomainJpaEntityDoesNotExist_shouldReturnNull() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);

        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        Optional<DomainEntityJpaEntity> actualDomainJpaEntity =
                domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(Location.class);

        assertTrue(actualDomainJpaEntity.isEmpty());
    }

    @Test
    public void findJpaDomainEntityByClass_whenDomainJpaEntityExists_shouldReturnBeReturned() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);

        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        Optional<DomainEntityJpaEntity> actualDomainJpaEntity =
                domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(Property.class);

        assertTrue(actualDomainJpaEntity.isPresent());
        assertEquals(Property.class.getName(), actualDomainJpaEntity.get().getName());
    }

    @Test
    public void findJpaDataTypeByClass_whenJpaDataTypeDoesNotExist_shouldReturnNull() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);

        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        Optional<DataTypeJpaEntity> jpaDataType =
                domainEntitySeedingTaskStateService.findJpaDataTypeByClass(Integer.class);

        assertTrue(jpaDataType.isEmpty());
    }

    @Test
    public void findJpaDataTypeByClass_whenJpaDataTypeExists_shouldBeReturned() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);

        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        Optional<DataTypeJpaEntity> jpaDataType =
                domainEntitySeedingTaskStateService.findJpaDataTypeByClass(String.class);

        assertTrue(jpaDataType.isPresent());
        assertEquals(String.class.getName(), jpaDataType.get().getFullClassName());
    }

    @Test
    public void getNewDomainEntityPropertiesOrderedByEntity_shouldNotContainOldProperties() {
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, "name");
        entityProperty.setNew(false);
        List<DomainEntityProperty> domainEntityProperties = List.of(entityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);

        Map<Class<?>, List<DomainEntityProperty>> result =
                domainEntitySeedingTaskStateService.getNewDomainEntityPropertiesOrderedByEntity();

        assertTrue(result.isEmpty());
    }

    @Test
    public void getNewDomainEntityPropertiesOrderedByEntity_shouldContainOnlyNewProperties() {
        DomainEntityProperty oldEntityProperty = new DomainEntityProperty(Property.class, String.class, "name");
        oldEntityProperty.setNew(false);
        DomainEntityProperty newEntityProperty = new DomainEntityProperty(Location.class, String.class, "street");
        newEntityProperty.setNew(true);
        List<DomainEntityProperty> domainEntityProperties = List.of(oldEntityProperty, newEntityProperty);

        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);

        Map<Class<?>, List<DomainEntityProperty>> result =
                domainEntitySeedingTaskStateService.getNewDomainEntityPropertiesOrderedByEntity();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        result.values()
                .forEach(entityProperties ->
                        entityProperties.forEach(property ->
                                assertTrue(property.isNew()))
                );
    }

    @Test
    public void addDataJpaType_whenTheInputDataTypeExistsAlready_shouldReturnFalse() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);

        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        DataTypeJpaEntity dataTypeJpaEntity = new DataTypeJpaEntity();
        dataTypeJpaEntity.setFullClassName(String.class.getName());

        boolean actualResult = domainEntitySeedingTaskStateService.addDataJpaType(dataTypeJpaEntity);

        assertFalse(actualResult);
    }

    @Test
    public void addDataJpaType_whenTheInputDataTypeDoesNotExist_shouldBeAddedAndReturnTrue() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        DataTypeJpaEntity dataTypeJpaEntity = new DataTypeJpaEntity();
        dataTypeJpaEntity.setFullClassName(Double.class.getName());

        boolean actualResult = domainEntitySeedingTaskStateService.addDataJpaType(dataTypeJpaEntity);
        Optional<DataTypeJpaEntity> actualDataJpaResult = domainEntitySeedingTaskStateService.findJpaDataTypeByClass(Double.class);

        assertTrue(actualResult);
        assertTrue(actualDataJpaResult.isPresent());
        assertEquals(Double.class.getName(), actualDataJpaResult.get().getFullClassName());
    }

    @Test
    public void addDataJpaType_whenTheInputDataTypeDoesNotExist_shouldMarkTypeAsUsed() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        DataTypeJpaEntity dataTypeJpaEntity = new DataTypeJpaEntity();
        dataTypeJpaEntity.setFullClassName(Double.class.getName());

        boolean actualResult = domainEntitySeedingTaskStateService.addDataJpaType(dataTypeJpaEntity);
        assertTrue(actualResult);

        List<DataTypeJpaEntity> unusedDataJpaTypes = domainEntitySeedingTaskStateService.getUnusedDataJpaTypes();
        assertThat(unusedDataJpaTypes, hasSize(1));
        assertThat(unusedDataJpaTypes, not(hasItem(dataTypeJpaEntity)));
    }

    @Test
    public void addDomainJpaEntity_whenTheInputDomainJpaEntityExistsAlready_shouldReturnFalse() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);

        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        DomainEntityJpaEntity domainEntityJpaEntity = new DomainEntityJpaEntity();
        domainEntityJpaEntity.setName(Property.class.getName());

        boolean actualResult = domainEntitySeedingTaskStateService.addDomainJpaEntity(domainEntityJpaEntity);

        assertFalse(actualResult);
    }

    @Test
    public void addDomainJpaEntity_whenTheInputDomainJpaEntityDoesNotExist_shouldBeAddedAndReturnTrue() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        DomainEntityJpaEntity domainEntityJpaEntity = new DomainEntityJpaEntity();
        domainEntityJpaEntity.setName(Location.class.getName());

        boolean actualResult = domainEntitySeedingTaskStateService.addDomainJpaEntity(domainEntityJpaEntity);

        Optional<DomainEntityJpaEntity> actualDomainEntityJpaResult = domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(Location.class);

        assertTrue(actualResult);
        assertTrue(actualDomainEntityJpaResult.isPresent());
        assertEquals(Location.class.getName(), actualDomainEntityJpaResult.get().getName());
    }

    @Test
    public void addDomainJpaEntity_whenTheInputDomainEntityDoesNotExist_shouldMarkEntityAsUsed() {
        DomainEntityPropertyJpaEntity entityJpaProperty = initDomainEntityJpaProperty(Property.class, String.class, "description");
        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = List.of(entityJpaProperty);
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);

        DomainEntityJpaEntity domainEntityJpaEntity = new DomainEntityJpaEntity();
        domainEntityJpaEntity.setName(Location.class.getName());

        boolean actualResult = domainEntitySeedingTaskStateService.addDomainJpaEntity(domainEntityJpaEntity);
        assertTrue(actualResult);

        List<DomainEntityJpaEntity> unusedDataJpaTypes = domainEntitySeedingTaskStateService.getUnusedDomainJpaEntities();
        assertThat(unusedDataJpaTypes, hasSize(1));
        assertThat(unusedDataJpaTypes, not(hasItem(domainEntityJpaEntity)));
    }

    @Test
    public void getUnusedDomainJpaEntities_whenAllEntitiesAreUsed_shouldReturnEmptyResult() {
        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, "name");
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(domainEntityPropertyJpaEntity));

        domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(Property.class);

        List<DomainEntityJpaEntity> result = domainEntitySeedingTaskStateService.getUnusedDomainJpaEntities();

        assertTrue(result.isEmpty());
    }

    @Test
    public void getUnusedDomainJpaEntities_whenThereAreUnusedEntities_shouldReturnThem() {
        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, "name");
        DomainEntityPropertyJpaEntity anotherEntityPropertyJpaEntity = initDomainEntityJpaProperty(Location.class, String.class, "name");
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(domainEntityPropertyJpaEntity, anotherEntityPropertyJpaEntity));

        domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(Property.class);

        List<DomainEntityJpaEntity> result = domainEntitySeedingTaskStateService.getUnusedDomainJpaEntities();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(Location.class.getName(), result.get(0).getName());
    }


    @Test
    public void getUnusedDataJpaTypes_whenAllTypesAreUsed_shouldReturnEmptyResult() {
        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, "name");
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(domainEntityPropertyJpaEntity));

        domainEntitySeedingTaskStateService.findJpaDataTypeByClass(String.class);

        List<DataTypeJpaEntity> result = domainEntitySeedingTaskStateService.getUnusedDataJpaTypes();

        assertTrue(result.isEmpty());
    }

    @Test
    public void getUnusedDataJpaTypes_whenThereAreUnusedTypes_shouldReturnThem() {
        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = initDomainEntityJpaProperty(Property.class, String.class, "name");
        DomainEntityPropertyJpaEntity anotherEntityPropertyJpaEntity = initDomainEntityJpaProperty(Location.class, Double.class, "stars");
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(List.of(domainEntityPropertyJpaEntity, anotherEntityPropertyJpaEntity));

        domainEntitySeedingTaskStateService.findJpaDataTypeByClass(String.class);

        List<DataTypeJpaEntity> result = domainEntitySeedingTaskStateService.getUnusedDataJpaTypes();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(Double.class.getName(), result.get(0).getFullClassName());
    }
}
