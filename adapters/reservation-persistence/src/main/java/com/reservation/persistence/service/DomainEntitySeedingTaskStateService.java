package com.reservation.persistence.service;

import com.reservation.common.processor.DomainEntityProperty;
import com.reservation.persistence.core.entity.DataTypeJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityJpaEntity;
import com.reservation.persistence.core.entity.DomainEntityPropertyJpaEntity;

import java.util.*;
import java.util.stream.Collectors;

public class DomainEntitySeedingTaskStateService {

    private static final boolean IS_JPA_ENTITY_USED = true;

    private List<DomainEntityProperty> domainEntityProperties;

    private Map<DomainEntityJpaEntity, Boolean> domainJpaEntities;

    private Map<DataTypeJpaEntity, Boolean> dataJpaTypes;

    public DomainEntitySeedingTaskStateService() {
        this.domainEntityProperties = new ArrayList<>();
        this.domainJpaEntities = new HashMap<>();
        this.dataJpaTypes = new HashMap<>();
    }

    private void setDomainJpaEntitiesAndDataJpaTypesFrom(List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties) {
        domainEntityJpaProperties
                .forEach(property -> {
                    DomainEntityJpaEntity domainEntity = property.getDomainEntity();
                    DataTypeJpaEntity dataType = property.getDataType();
                    domainJpaEntities.putIfAbsent(domainEntity, !IS_JPA_ENTITY_USED);
                    dataJpaTypes.putIfAbsent(dataType, !IS_JPA_ENTITY_USED);
                });
    }

    private void markDomainJpaEntityAsUsed(DomainEntityJpaEntity domainJpaEntity) {
        domainJpaEntities.replace(domainJpaEntity, IS_JPA_ENTITY_USED);
    }

    private void markDataJpaTypeAsUsed(DataTypeJpaEntity dataTypeJpaEntity) {
        dataJpaTypes.replace(dataTypeJpaEntity, IS_JPA_ENTITY_USED);
    }

    public void setDomainEntityProperties(List<DomainEntityProperty> domainEntityProperties) {
        this.domainEntityProperties = domainEntityProperties;
    }

    public void setDomainEntityJpaProperties(List<DomainEntityPropertyJpaEntity> domainEntityJpaProperties) {
        setDomainJpaEntitiesAndDataJpaTypesFrom(domainEntityJpaProperties);
    }

    public DomainEntityProperty findPropertyByEntityNameAndPropertyName(String domainEntityName, String propertyName) {
        Optional<DomainEntityProperty> propertyCandidate = domainEntityProperties.stream()
                .filter(property -> property.getDomainEntityFullName().equals(domainEntityName) && property.getName().equals(propertyName))
                .findFirst();

        if (propertyCandidate.isEmpty()) {
            return null;
        }

        DomainEntityProperty entityProperty = propertyCandidate.get();
        entityProperty.setNew(false);

        //Mark Data Type and Domain Entity of the given property as marked
        findJpaDomainEntityByClass(entityProperty.getDomainEntityClass());
        findJpaDataTypeByClass(entityProperty.getDataTypeClass());

        return entityProperty;
    }

    public Optional<DomainEntityJpaEntity> findJpaDomainEntityByClass(Class<?> domainEntityClass) {
        return domainJpaEntities.keySet()
                .stream()
                .filter(domainJpaEntity -> domainJpaEntity.getName().equals(domainEntityClass.getName()))
                .peek(this::markDomainJpaEntityAsUsed)
                .findFirst();
    }

    public Optional<DataTypeJpaEntity> findJpaDataTypeByClass(Class<?> propertyDataType) {
        return dataJpaTypes.keySet()
                .stream()
                .filter(dataJpaType -> dataJpaType.getFullClassName().equals(propertyDataType.getName()))
                .peek(this::markDataJpaTypeAsUsed)
                .findFirst();
    }

    public Map<Class<?>, List<DomainEntityProperty>> getNewDomainEntityPropertiesOrderedByEntity() {
        return domainEntityProperties.stream()
                .filter(DomainEntityProperty::isNew)
                .collect(Collectors.groupingBy(
                        DomainEntityProperty::getDomainEntityClass,
                        Collectors.mapping(property -> property, Collectors.toList()))
                );
    }

    public boolean addDataJpaType(DataTypeJpaEntity jpaDataType) {
        Boolean previousValue = dataJpaTypes.putIfAbsent(jpaDataType, IS_JPA_ENTITY_USED);
        return Objects.isNull(previousValue);
    }

    public boolean addDomainJpaEntity(DomainEntityJpaEntity domainJpaEntity) {
        Boolean previousValue = domainJpaEntities.putIfAbsent(domainJpaEntity, IS_JPA_ENTITY_USED);
        return Objects.isNull(previousValue);
    }

    public List<DomainEntityJpaEntity> getUnusedDomainJpaEntities() {
        return domainJpaEntities.entrySet()
                .stream()
                .filter(domainJpaEntityEntry -> !domainJpaEntityEntry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<DataTypeJpaEntity> getUnusedDataJpaTypes() {
        return dataJpaTypes.entrySet()
                .stream()
                .filter(dataJpaTypeEntry -> !dataJpaTypeEntry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
