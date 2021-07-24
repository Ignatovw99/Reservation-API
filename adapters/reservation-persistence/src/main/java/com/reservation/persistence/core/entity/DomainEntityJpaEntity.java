package com.reservation.persistence.core.entity;

import com.reservation.common.base.NumericJpaIdentifier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "domain_entities")
public class DomainEntityJpaEntity extends NumericJpaIdentifier {

    @Column(name = "name")
    @NotNull
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
        if (!(o instanceof DomainEntityJpaEntity)) return false;
        if (Objects.nonNull(getId()) && !super.equals(o)) return false;
        DomainEntityJpaEntity that = (DomainEntityJpaEntity) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        return "DomainEntityJpaEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
