package com.reservation.application.domain.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class AppUserTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(AppUser.class)
                .withRedefinedSuperclass()
                .withOnlyTheseFields("id", "name", "username", "email")
                .verify();
    }
}
