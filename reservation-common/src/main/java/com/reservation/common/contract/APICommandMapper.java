package com.reservation.common.contract;

/**
 * This is an abstract mapper for mapping an API model to a command
 * @param <A> API model type
 * @param <C> Command type
 */
public interface APICommandMapper<A, C> {

    C toCommand(A apiModel);
}
