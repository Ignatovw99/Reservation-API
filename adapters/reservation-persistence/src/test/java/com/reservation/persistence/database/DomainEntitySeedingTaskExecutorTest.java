package com.reservation.persistence.database;

import com.reservation.application.domain.entity.DomainEntityPropertiesWrapper;
import com.reservation.application.domain.entity.Location;
import com.reservation.application.domain.entity.Property;
import com.reservation.common.processor.DomainEntityProperty;
import com.reservation.persistence.PersistenceContextTest;
import com.reservation.persistence.core.entity.DataTypeJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityPropertyJpaEntity;
import com.reservation.persistence.core.repository.DataTypeJpaRepository;
import com.reservation.persistence.core.repository.DomainEntityJpaRepository;
import com.reservation.persistence.core.repository.DomainEntityPropertyJpaRepository;
import liquibase.database.core.MariaDBDatabase;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceContextTest(properties = "spring.liquibase.contexts=dev")
class DomainEntitySeedingTaskExecutorTest {

    @Autowired
    private DomainEntityJpaRepository domainEntityJpaRepository;

    @Autowired
    private DataTypeJpaRepository dataTypeJpaRepository;

    @Autowired
    private DomainEntityPropertyJpaRepository domainEntityPropertyJpaRepository;

    private DomainEntitySeedingTaskExecutor domainEntitySeedingTaskExecutor;

    private DomainEntityJpaEntity persistDomainEntity(Class<?> entityClass) {
        DomainEntityJpaEntity domainEntityJpa = new DomainEntityJpaEntity();
        domainEntityJpa.setName(entityClass.getName());
        return domainEntityJpaRepository.save(domainEntityJpa);
    }

    private DataTypeJpaEntity persistDataType(Class<?> typeClass) {
        DataTypeJpaEntity dataTypeJpa = new DataTypeJpaEntity();
        dataTypeJpa.setFullClassName(typeClass.getName());
        return dataTypeJpaRepository.save(dataTypeJpa);
    }

    private DomainEntityPropertyJpaEntity persistDomainEntityProperty(String propertyName, DomainEntityJpaEntity domainEntityJpa, DataTypeJpaEntity dataTypeJpa) {
        DomainEntityPropertyJpaEntity domainEntityPropertyJpa = new DomainEntityPropertyJpaEntity();
        domainEntityPropertyJpa.setName(propertyName);
        domainEntityPropertyJpa.setDomainEntity(domainEntityJpa);
        domainEntityPropertyJpa.setDataType(dataTypeJpa);
        return domainEntityPropertyJpaRepository.save(domainEntityPropertyJpa);
    }

    @BeforeEach
    void setUp() throws SetupException {
        domainEntitySeedingTaskExecutor = new DomainEntitySeedingTaskExecutor();
        //Clear all records to work with an empty database
        domainEntityPropertyJpaRepository.deleteAll();
        domainEntityPropertyJpaRepository.flush();
        domainEntityJpaRepository.deleteAll();
        domainEntityJpaRepository.flush();
        dataTypeJpaRepository.deleteAll();
        dataTypeJpaRepository.flush();
    }

    @Test
    public void executeTask_whenDomainEntitiesAreAvailable_shouldPersistInAllDomainRelatedTables() throws CustomChangeException {
        List<DomainEntityProperty> domainEntityProperties = DomainEntityPropertiesWrapper.getPropertiesOfDomainEntities();
        assertFalse(domainEntityProperties.isEmpty());

        long domainEntityCountBeforeExecution = domainEntityJpaRepository.count();
        long domainEntityPropertyBeforeExecution = domainEntityPropertyJpaRepository.count();
        long dataTypeBeforeExecution = dataTypeJpaRepository.count();

        assertEquals(0, domainEntityCountBeforeExecution);
        assertEquals(0, domainEntityPropertyBeforeExecution);
        assertEquals(0, dataTypeBeforeExecution);

        domainEntitySeedingTaskExecutor.execute(new MariaDBDatabase());

        long domainEntityCountAfterExecution = domainEntityJpaRepository.count();
        long domainEntityPropertyAfterExecution = domainEntityPropertyJpaRepository.count();
        long dataTypeAfterExecution = dataTypeJpaRepository.count();

        assertNotEquals(0, domainEntityCountAfterExecution);
        assertNotEquals(0, dataTypeAfterExecution);
        assertEquals(DomainEntityPropertiesWrapper.getPropertiesOfDomainEntities().size(), domainEntityPropertyAfterExecution);
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyIsNoLongerRelevant_shouldBeDeleted() {
        DomainEntityJpaEntity domainEntityJpa = persistDomainEntity(Property.class);
        DataTypeJpaEntity dataTypeJpa = persistDataType(String.class);
        DomainEntityPropertyJpaEntity domainEntityPropertyJpa = persistDomainEntityProperty("username", domainEntityJpa, dataTypeJpa);

        Long entityPropertyId = domainEntityPropertyJpa.getId();
        Optional<DomainEntityPropertyJpaEntity> propertyJpaCandidate = domainEntityPropertyJpaRepository.findById(entityPropertyId);
        assertTrue(propertyJpaCandidate.isPresent());

        DomainEntityProperty entityProperty = new DomainEntityProperty(Location.class, String.class, "town");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        propertyJpaCandidate = domainEntityPropertyJpaRepository.findById(entityPropertyId);
        assertTrue(propertyJpaCandidate.isEmpty());
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyIsAlreadyPersistedAndIsNotChanged_shouldNotBeModified() {
        DomainEntityJpaEntity domainEntityJpa = persistDomainEntity(Property.class);
        DataTypeJpaEntity dataTypeJpa = persistDataType(String.class);
        DomainEntityPropertyJpaEntity domainEntityPropertyJpa = persistDomainEntityProperty("name", domainEntityJpa, dataTypeJpa);

        Long entityPropertyId = domainEntityPropertyJpa.getId();
        Optional<DomainEntityPropertyJpaEntity> propertyJpaCandidate = domainEntityPropertyJpaRepository.findById(entityPropertyId);
        assertTrue(propertyJpaCandidate.isPresent());

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, "name");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        propertyJpaCandidate = domainEntityPropertyJpaRepository.findById(entityPropertyId);
        assertTrue(propertyJpaCandidate.isPresent());
        DomainEntityPropertyJpaEntity entityPropertyJpa = propertyJpaCandidate.get();
        assertEquals("name", entityPropertyJpa.getName());
        assertEquals(domainEntityJpa, entityPropertyJpa.getDomainEntity());
        assertEquals(dataTypeJpa, entityPropertyJpa.getDataType());
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyDataTypeIsDifferent_shouldChangeIt() {
        Class<Double> newPropertyClass = Double.class;
        DomainEntityJpaEntity domainEntityJpa = persistDomainEntity(Property.class);
        DataTypeJpaEntity oldDataTypeJpa = persistDataType(Integer.class);
        DataTypeJpaEntity newDataTypeJpa = persistDataType(newPropertyClass);
        DomainEntityPropertyJpaEntity domainEntityPropertyJpa = persistDomainEntityProperty("stars", domainEntityJpa, oldDataTypeJpa);

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, newPropertyClass, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        Optional<DomainEntityPropertyJpaEntity> propertyJpaCandidate =
                domainEntityPropertyJpaRepository.findById(domainEntityPropertyJpa.getId());
        assertTrue(propertyJpaCandidate.isPresent());
        DomainEntityPropertyJpaEntity entityPropertyJpa = propertyJpaCandidate.get();

        assertEquals(newDataTypeJpa, entityPropertyJpa.getDataType());
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyDataTypeIsDifferentButItDoesNotExistInTheDB_shouldCreateIt() {
        DomainEntityJpaEntity domainEntityJpa = persistDomainEntity(Property.class);
        DataTypeJpaEntity oldDataTypeJpa = persistDataType(Integer.class);
        DomainEntityPropertyJpaEntity domainEntityPropertyJpa = persistDomainEntityProperty("stars", domainEntityJpa, oldDataTypeJpa);

        Class<Double> newPropertyClass = Double.class;
        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, newPropertyClass, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        DataTypeJpaEntity dataTypeBeforeExecution = dataTypeJpaRepository.findByFullClassNameEquals(newPropertyClass.getName());
        assertNull(dataTypeBeforeExecution);

        boolean existDomainEntityProperty = domainEntityPropertyJpaRepository.findById(domainEntityPropertyJpa.getId())
                .isPresent();
        assertTrue(existDomainEntityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        DataTypeJpaEntity dataTypeAfterExecution = dataTypeJpaRepository.findByFullClassNameEquals(newPropertyClass.getName());
        assertNotNull(dataTypeAfterExecution);
        assertEquals(newPropertyClass.getName(), dataTypeAfterExecution.getFullClassName());
    }

    @Test
    public void persistDomainRelatedData_whenDomainEntityDoesNotExist_shouldPersistIt() {
        DomainEntityJpaEntity domainJpaEntity = domainEntityJpaRepository.findByName(Property.class.getName());
        assertNull(domainJpaEntity);

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, Double.class, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        domainJpaEntity = domainEntityJpaRepository.findByName(Property.class.getName());
        assertNotNull(domainJpaEntity);
    }

    @Test
    public void persistDomainRelatedData_whenDataTypeDoesNotExist_shouldPersistIt() {
        DataTypeJpaEntity dataJpaType = dataTypeJpaRepository.findByFullClassNameEquals(Double.class.getName());
        assertNull(dataJpaType);

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, Double.class, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        dataJpaType = dataTypeJpaRepository.findByFullClassNameEquals(Double.class.getName());
        assertNotNull(dataJpaType);
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyDoesNotExist_shouldPersistIt() {
        long domainEntityJpaPropertiesCount = domainEntityPropertyJpaRepository.count();
        assertEquals(0, domainEntityJpaPropertiesCount);

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, Double.class, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        domainEntityJpaPropertiesCount = domainEntityPropertyJpaRepository.count();
        assertEquals(1, domainEntityJpaPropertiesCount);

        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = domainEntityPropertyJpaRepository.findAll().get(0);
        assertEquals("stars", domainEntityPropertyJpaEntity.getName());
        assertEquals(Property.class.getName(), domainEntityPropertyJpaEntity.getDomainEntity().getName());
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyDoesNotExistButTheEntityExists_shouldNotPersistTheEntityAgain() {
        persistDomainEntity(Property.class);
        long domainJpaEntitiesCount = domainEntityJpaRepository.count();
        assertEquals(1, domainJpaEntitiesCount);

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, Double.class, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        domainJpaEntitiesCount = domainEntityJpaRepository.count();
        assertEquals(1, domainJpaEntitiesCount);
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyDoesNotExistButThePropertyTypeExists_shouldNotPersistTheDataTypeAgain() {
        persistDataType(Double.class);
        long dataJpaTypesCount = dataTypeJpaRepository.count();
        assertEquals(1, dataJpaTypesCount);

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, Double.class, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        dataJpaTypesCount = dataTypeJpaRepository.count();
        assertEquals(1, dataJpaTypesCount);
    }

    @Test
    public void persistDomainRelatedData_whenEntityPropertyExistsAlready_shouldNotBePersistedAgain() {
        DomainEntityJpaEntity domainEntityJpa = persistDomainEntity(Property.class);
        DataTypeJpaEntity dataTypeJpa = persistDataType(String.class);
        DomainEntityPropertyJpaEntity domainEntityPropertyJpa = persistDomainEntityProperty("name", domainEntityJpa, dataTypeJpa);

        Long entityPropertyId = domainEntityPropertyJpa.getId();
        Optional<DomainEntityPropertyJpaEntity> propertyJpaCandidate = domainEntityPropertyJpaRepository.findById(entityPropertyId);
        assertTrue(propertyJpaCandidate.isPresent());

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, String.class, "name");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        propertyJpaCandidate = domainEntityPropertyJpaRepository.findById(entityPropertyId);
        assertTrue(propertyJpaCandidate.isPresent());
        long propertyCount = domainEntityPropertyJpaRepository.count();
        assertEquals(1, propertyCount);
    }

    @Test
    public void persistDomainRelatedData_whenDomainEntityIsNoLongerRelevant_shouldBeDeleted() {
        DataTypeJpaEntity dataTypeJpaEntity = persistDataType(String.class);
        DomainEntityJpaEntity domainEntityJpaEntity = persistDomainEntity(Property.class);
        persistDomainEntityProperty("name", domainEntityJpaEntity, dataTypeJpaEntity);

        assertNotNull(domainEntityJpaRepository.findByName(domainEntityJpaEntity.getName()));

        DomainEntityProperty entityProperty = new DomainEntityProperty(Location.class, String.class, "street");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        assertNull(domainEntityJpaRepository.findByName(domainEntityJpaEntity.getName()));
    }

    @Test
    public void persistDomainRelatedData_whenDataTypeIsNoLongerRelevant_shouldBeDeleted() {
        DataTypeJpaEntity dataTypeJpaEntity = persistDataType(String.class);
        DomainEntityJpaEntity domainEntityJpaEntity = persistDomainEntity(Property.class);
        persistDomainEntityProperty("name", domainEntityJpaEntity, dataTypeJpaEntity);

        assertNotNull(dataTypeJpaRepository.findByFullClassNameEquals(dataTypeJpaEntity.getFullClassName()));

        DomainEntityProperty entityProperty = new DomainEntityProperty(Property.class, Double.class, "stars");
        List<DomainEntityProperty> relevantEntityProperty = List.of(entityProperty);

        domainEntitySeedingTaskExecutor.processDomainDataPersistence(relevantEntityProperty);

        assertNull(dataTypeJpaRepository.findByFullClassNameEquals(dataTypeJpaEntity.getFullClassName()));
    }
}
