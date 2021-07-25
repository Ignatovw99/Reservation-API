package com.reservation.common.contract;

/**
 * This is an abstract mapper for mapping a command to a domain entity
 * @param <C> Command type
 * @param <D> Domain entity type
 */
public interface CommandDomainEntityMapper<C, D> {

    D toDomainEntity(C command);
}
