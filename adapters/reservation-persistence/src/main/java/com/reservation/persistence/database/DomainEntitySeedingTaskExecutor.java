package com.reservation.persistence.database;

import com.reservation.application.domain.entity.DomainEntityPropertiesWrapper;
import com.reservation.common.processor.DomainEntityProperty;
import com.reservation.persistence.config.BeanAwareSpringLiquibase;
import com.reservation.persistence.core.entity.DataTypeJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityPropertyJpaEntity;
import com.reservation.persistence.core.repository.DataTypeJpaRepository;
import com.reservation.persistence.core.repository.DomainEntityJpaRepository;
import com.reservation.persistence.core.repository.DomainEntityPropertyJpaRepository;
import com.reservation.persistence.service.DomainEntitySeedingTaskStateService;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.springframework.context.ApplicationContextException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DomainEntitySeedingTaskExecutor implements CustomTaskChange {

    private static final String SUCCESSFUL_MESSAGE = "Domain Entity Data custom task change executed successfully";

    private final DomainEntitySeedingTaskStateService domainEntitySeedingTaskStateService = new DomainEntitySeedingTaskStateService();

    private DomainEntityJpaRepository domainEntityJpaRepository;

    private DomainEntityPropertyJpaRepository domainEntityPropertyJpaRepository;

    private DataTypeJpaRepository dataTypeJpaRepository;

    private ResourceAccessor resourceAccessor;

    private void injectJpaRepositories() {
        domainEntityJpaRepository = BeanAwareSpringLiquibase.getBean(DomainEntityJpaRepository.class);
        domainEntityPropertyJpaRepository = BeanAwareSpringLiquibase.getBean(DomainEntityPropertyJpaRepository.class);
        dataTypeJpaRepository = BeanAwareSpringLiquibase.getBean(DataTypeJpaRepository.class);
    }

    private void setUpDomainEntitySeedingStateService(List<DomainEntityProperty> domainEntityProperties, List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties) {
        domainEntitySeedingTaskStateService.setDomainEntityProperties(domainEntityProperties);
        domainEntitySeedingTaskStateService.setDomainEntityJpaProperties(domainEntityJpaProperties);
    }

    private boolean shouldPropertyDataTypeBeChanged(DomainEntityPropertyJpaEntity domainEntityJpaProperty, Class<?> propertyCurrentDataType) {
        return !domainEntityJpaProperty.hasDataType(propertyCurrentDataType.getName());
    }

    private DomainEntityJpaEntity getJpaDomainEntityByClass(Class<?> domainEntityClass) {
        DomainEntityJpaEntity domainJpaEntity =
                domainEntitySeedingTaskStateService.findJpaDomainEntityByClass(domainEntityClass)
                        .orElse(domainEntityJpaRepository.findByName(domainEntityClass.getName()));

        if (Objects.isNull(domainJpaEntity)) {
            domainJpaEntity = new DomainEntityJpaEntity();
            domainJpaEntity.setName(domainEntityClass.getName());
            domainEntityJpaRepository.save(domainJpaEntity);
        }
        return domainJpaEntity;
    }

    private DataTypeJpaEntity getJpaDataTypeByClass(Class<?> propertyDataType) {
        DataTypeJpaEntity jpaDataType =
                domainEntitySeedingTaskStateService.findJpaDataTypeByClass(propertyDataType)
                        .orElse(dataTypeJpaRepository.findByFullClassNameEquals(propertyDataType.getName()));

        if (Objects.isNull(jpaDataType)) {
            jpaDataType = new DataTypeJpaEntity();
            jpaDataType.setFullClassName(propertyDataType.getName());
            dataTypeJpaRepository.save(jpaDataType);
            domainEntitySeedingTaskStateService.addDataJpaType(jpaDataType);
        }
        return jpaDataType;
    }

    private void syncDomainEntityJpaProperty(DomainEntityPropertyJpaEntity domainEntityJpaProperty) {
        String domainEntityName = domainEntityJpaProperty.getDomainEntity().getName();
        String propertyName = domainEntityJpaProperty.getName();

        DomainEntityProperty currentEntityProperty = domainEntitySeedingTaskStateService.findPropertyByEntityNameAndPropertyName(domainEntityName, propertyName);

        if (Objects.isNull(currentEntityProperty)) {
            domainEntityPropertyJpaRepository.delete(domainEntityJpaProperty);
            return;
        }

        Class<?> propertyDataType = currentEntityProperty.getDataTypeClass();

        if (shouldPropertyDataTypeBeChanged(domainEntityJpaProperty, propertyDataType)) {

            DataTypeJpaEntity jpaDataType = getJpaDataTypeByClass(propertyDataType);
            domainEntityJpaProperty.setDataType(jpaDataType);

            domainEntityPropertyJpaRepository.save(domainEntityJpaProperty);
        }
    }

    private DomainEntityPropertyJpaEntity persistEntityProperty(DomainEntityProperty entityProperty) {
        DataTypeJpaEntity jpaPropertyType = getJpaDataTypeByClass(entityProperty.getDataTypeClass());
        DomainEntityJpaEntity domainEntityJpaEntity = getJpaDomainEntityByClass(entityProperty.getDomainEntityClass());

        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = new DomainEntityPropertyJpaEntity();
        domainEntityPropertyJpaEntity.setDomainEntity(domainEntityJpaEntity);
        domainEntityPropertyJpaEntity.setName(entityProperty.getName());
        domainEntityPropertyJpaEntity.setDataType(jpaPropertyType);

        domainEntitySeedingTaskStateService.addDataJpaType(jpaPropertyType);
        domainEntitySeedingTaskStateService.addDomainJpaEntity(domainEntityJpaEntity);
        return domainEntityPropertyJpaEntity;
    }

    private void persistAllNewEntityProperties(Class<?> domainEntityClass, List<DomainEntityProperty> entityNewProperties) {
        List<DomainEntityPropertyJpaEntity> domainEntityNewJpaProperties = new ArrayList<>();
        entityNewProperties.forEach(entityProperty -> {
            DomainEntityPropertyJpaEntity entityNewProperty = persistEntityProperty(entityProperty);
            domainEntityNewJpaProperties.add(entityNewProperty);
        });
        domainEntityPropertyJpaRepository.saveAll(domainEntityNewJpaProperties);
    }

    public void processDomainDataPersistence(List<DomainEntityProperty> domainEntityProperties) {

        injectJpaRepositories();

        List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties = domainEntityPropertyJpaRepository.findAll();
        setUpDomainEntitySeedingStateService(domainEntityProperties, domainEntityJpaProperties);

        domainEntityJpaProperties
                .forEach(this::syncDomainEntityJpaProperty);

        domainEntitySeedingTaskStateService.getNewDomainEntityPropertiesOrderedByEntity()
                .forEach(this::persistAllNewEntityProperties);

        domainEntitySeedingTaskStateService.getUnusedDataJpaTypes()
                .forEach(dataJpaType -> dataTypeJpaRepository.delete(dataJpaType));

        domainEntitySeedingTaskStateService.getUnusedDomainJpaEntities()
                .forEach(domainJpaEntity -> domainEntityJpaRepository.delete(domainJpaEntity));
    }

    @Override
    public void execute(Database database) throws CustomChangeException {
        try {
            final List<DomainEntityProperty> domainEntityProperties =
                    DomainEntityPropertiesWrapper.getPropertiesOfDomainEntities();

            processDomainDataPersistence(domainEntityProperties);
        } catch (ApplicationContextException ex) {
            throw new CustomChangeException("Spring repos can not be injected to this Liquibase change task.");
        }
    }

    @Override
    public String getConfirmationMessage() {
        return SUCCESSFUL_MESSAGE;
    }

    @Override
    public void setUp() {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }
}