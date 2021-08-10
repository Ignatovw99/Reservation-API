package com.reservation.application.domain.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class PropertyTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(Property.class)
                .withRedefinedSuperclass()
                .withOnlyTheseFields("id", "name")
                .verify();
    }
}
