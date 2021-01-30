package com.reservation.application.domain.entity;

import com.reservation.application.domain.entity.base.NumericIdentifier;

import java.util.Objects;

public class PropertyType extends NumericIdentifier {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyType)) return false;
        PropertyType that = (PropertyType) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "PropertyType{" +
                "id=" + super.getId() +
                "name='" + name + '\'' +
                '}';
    }
}
