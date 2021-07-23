package com.reservation.common.contract;

public interface Identifiable<T> {

    T getId();

    void setId(T id);
}
