package com.reservation.persistence.core.entity;

import com.reservation.persistence.core.base.NumericJpaEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "domain_entity_properties")
public class DomainEntityPropertyJpaEntity extends NumericJpaEntity {

    @Column(name = "name")
    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_entity_id")
    @NotNull
    private DomainEntityJpaEntity domainEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_type_id")
    private DataTypeJpaEntity dataType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DomainEntityJpaEntity getDomainEntity() {
        return domainEntity;
    }

    public void setDomainEntity(DomainEntityJpaEntity domainEntity) {
        this.domainEntity = domainEntity;
    }

    public DataTypeJpaEntity getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeJpaEntity dataType) {
        this.dataType = dataType;
    }

    public boolean hasDataType(String dataTypeFullName) {
        if (Objects.isNull(getDataType())) {
            return false;
        } else {
            return getDataType().getFullClassName().equals(dataTypeFullName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEntityPropertyJpaEntity)) return false;
        if (!super.equals(o)) return false;
        DomainEntityPropertyJpaEntity that = (DomainEntityPropertyJpaEntity) o;
        return getName().equals(that.getName()) &&
                getDomainEntity().equals(that.getDomainEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getDomainEntity());
    }

    @Override
    public String toString() {
        return "DomainEntityPropertyJpaEntity{" +
                "name='" + name + '\'' +
                ", domainEntity=" + domainEntity.getName() +
                '}';
    }
}
