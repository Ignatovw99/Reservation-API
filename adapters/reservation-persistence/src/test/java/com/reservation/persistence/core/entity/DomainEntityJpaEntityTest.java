package com.reservation.persistence.core.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class DomainEntityJpaEntityTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(DomainEntityJpaEntity.class)
                .withRedefinedSuperclass()
                .verify();
    }
}
