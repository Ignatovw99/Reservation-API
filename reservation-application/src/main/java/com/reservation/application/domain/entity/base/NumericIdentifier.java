package com.reservation.application.domain.entity.base;

import com.reservation.common.contract.Identifiable;

import java.util.Objects;

public class NumericIdentifier implements Identifiable<Long> {

    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumericIdentifier)) return false;
        NumericIdentifier that = (NumericIdentifier) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
