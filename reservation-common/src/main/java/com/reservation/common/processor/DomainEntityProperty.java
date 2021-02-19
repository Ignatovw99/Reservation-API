package com.reservation.common.processor;

import java.util.Objects;

public class DomainEntityProperty {

    private final Class<?> domainEntityClass;

    private final Class<?> dataTypeClass;

    private final String name;

    private boolean isNew = true;

    public DomainEntityProperty(Class<?> domainEntityClass, Class<?> dataTypeClass, String name) {
        this.domainEntityClass = domainEntityClass;
        this.dataTypeClass = dataTypeClass;
        this.name = name;
    }

    public String getDomainEntityFullName() {
        return domainEntityClass.getName();
    }

    public String getDataTypeFullName() {
        return dataTypeClass.getName();
    }

    public String getName() {
        return name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEntityProperty)) return false;
        DomainEntityProperty that = (DomainEntityProperty) o;
        return Objects.equals(getDomainEntityFullName(), that.getDomainEntityFullName()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDomainEntityFullName(), getName());
    }

    @Override
    public String toString() {
        return "DomainEntityProperty{" +
                "domainEntityClass=" + domainEntityClass +
                ", dataTypeClass=" + dataTypeClass +
                ", name='" + name + '\'' +
                '}';
    }
}
