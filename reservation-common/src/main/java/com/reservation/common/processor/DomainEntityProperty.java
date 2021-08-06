package com.reservation.common.processor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DomainEntityProperty {

    private final Class<?> domainEntityClass;

    private final Class<?> dataTypeClass;

    @EqualsAndHashCode.Include
    private final String name;

    private boolean isNew = true;

    @EqualsAndHashCode.Include
    public String getDomainEntityFullName() {
        return domainEntityClass.getName();
    }
}
