package com.reservation.application.domain.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class PropertyTypeTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(PropertyType.class)
                .withRedefinedSuperclass()
                .withOnlyTheseFields("id", "name")
                .verify();
    }
}
