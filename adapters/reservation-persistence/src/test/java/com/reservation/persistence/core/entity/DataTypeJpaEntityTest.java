package com.reservation.persistence.core.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class DataTypeJpaEntityTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(DataTypeJpaEntity.class)
                .withRedefinedSuperclass()
                .verify();
    }
}
