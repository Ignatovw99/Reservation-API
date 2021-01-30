package com.reservation.application.domain.entity.base;

import java.util.Objects;

public class NumericIdentifier {

    private Long id;

    public Long getId() {
        return id;
    }

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
