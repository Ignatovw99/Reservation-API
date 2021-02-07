package com.reservation.common.processor;

import java.util.Objects;

public class DomainEntityProperty<E, P> {

    private final Class<E> domainEntityDataType;

    private final Class<P> dataType;

    private final String name;

    public DomainEntityProperty(Class<E> domainEntityDataType, Class<P> dataType, String name) {
        this.domainEntityDataType = domainEntityDataType;
        this.dataType = dataType;
        this.name = name;
    }

    public String getDomainEntityDataType() {
        return domainEntityDataType.getSimpleName();
    }

    public String getDataType() {
        return dataType.getSimpleName();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEntityProperty)) return false;
        DomainEntityProperty<?, ?> that = (DomainEntityProperty<?, ?>) o;
        return Objects.equals(getDomainEntityDataType(), that.getDomainEntityDataType()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDomainEntityDataType(), getName());
    }

    @Override
    public String toString() {
        return "DomainEntityProperty{" +
                "domainEntityType=" + domainEntityDataType +
                ", type=" + dataType +
                ", name='" + name + '\'' +
                '}';
    }
}
