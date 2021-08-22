package com.reservation.application.domain.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AppUserTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(AppUser.class)
                .withRedefinedSuperclass()
                .withOnlyTheseFields("id", "name", "username", "email")
                .verify();
    }

    @Test
    public void getLogin_whenEmailIsNullAndUsernameIsNotNull_shouldReturnTheUsername() {
        String username = "username123";
        AppUser user = AppUser.builder()
                    .username(username)
                    .email(null)
                    .password("Password213!")
                    .name("Name")
                .build();

        String actual = user.getLogin();

        assertEquals(username, actual);
    }

    @Test
    public void getLogin_whenEmailIsNotNullAndUsernameIsNull_shouldReturnTheEmail() {
        String email = "email@host.com";
        AppUser user = AppUser.builder()
                    .username(null)
                    .email(email)
                    .password("Password213!")
                    .name("Name")
                .build();

        String actual = user.getLogin();

        assertEquals(email, actual);
    }

    @Test
    public void getLogin_whenUsernameAndEmailAreNotNull_shouldReturnUsername() {
        String email = "email@host.com";
        String username = "username123";

        AppUser user = AppUser.builder()
                    .username(username)
                    .email(email)
                    .password("Password213!")
                    .name("Name")
                .build();

        String actual = user.getLogin();

        assertEquals(username, actual);
        assertNotEquals(email, actual);
    }
}
