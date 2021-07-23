package com.reservation.persistence.core.entity;

import com.reservation.persistence.core.base.NumericJpaEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "data_types")
public class DataTypeJpaEntity extends NumericJpaEntity {

    @Column(name = "full_class_name")
    @NotNull
    private String fullClassName;

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataTypeJpaEntity)) return false;
        if (Objects.nonNull(getId()) && !super.equals(o)) return false;
        DataTypeJpaEntity that = (DataTypeJpaEntity) o;
        return getFullClassName().equals(that.getFullClassName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFullClassName());
    }

    @Override
    public String toString() {
        return "DataTypeJpaEntity{" +
                "fullClassName='" + fullClassName + '\'' +
                '}';
    }
}
