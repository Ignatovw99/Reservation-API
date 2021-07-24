package com.reservation.application.domain.entity;

import com.reservation.application.domain.entity.base.DomainEntity;
import com.reservation.common.base.NumericJpaIdentifier;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@DomainEntity
@Entity
@Table(name = "property_type")
public class PropertyType extends NumericJpaIdentifier {

    private String name;

    private Boolean requiresPrivate;

    private Boolean allowsMultipleRooms;

    private Boolean requiresAlternative;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequiresPrivate() {
        return requiresPrivate;
    }

    public void setRequiresPrivate(Boolean requiresPrivate) {
        this.requiresPrivate = requiresPrivate;
    }

    public Boolean getAllowsMultipleRooms() {
        return allowsMultipleRooms;
    }

    public void setAllowsMultipleRooms(Boolean allowsMultipleRooms) {
        this.allowsMultipleRooms = allowsMultipleRooms;
    }

    public Boolean getRequiresAlternative() {
        return requiresAlternative;
    }

    public void setRequiresAlternative(Boolean requiresAlternative) {
        this.requiresAlternative = requiresAlternative;
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
                "name='" + name + '\'' +
                ", requiresPrivate=" + requiresPrivate +
                ", allowsMultipleRooms=" + allowsMultipleRooms +
                ", requiresAlternative=" + requiresAlternative +
                '}';
    }
}
