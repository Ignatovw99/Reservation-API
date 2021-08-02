package com.reservation.common.contract;

/**
 * This is an abstract mapper for mapping a domain entity to an API model
 * @param <D> Domain entity type
 * @param <A> API model type
 */
public interface DomainEntityApiMapper<D, A> {

    A toApiModel(D domainEntity);
}
