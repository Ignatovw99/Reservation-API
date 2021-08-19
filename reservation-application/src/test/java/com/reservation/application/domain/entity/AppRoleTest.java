package com.reservation.application.domain.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class AppRoleTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(AppRole.class)
                .withRedefinedSuperclass()
                .withOnlyTheseFields("id", "name")
                .verify();
    }
}
