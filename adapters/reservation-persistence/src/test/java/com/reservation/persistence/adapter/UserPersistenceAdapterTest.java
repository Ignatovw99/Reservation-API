package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.service.RegisterUserServiceTest;
import com.reservation.persistence.PersistenceContextTest;
import com.reservation.persistence.core.repository.AppUserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceContextTest
public class UserPersistenceAdapterTest {

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Autowired
    private AppUserJpaRepository appUserRepository;

    @Test
    public void findUserByUsername_whenUsernameDoesNotExist_shouldReturnNull() {
        AppUser user = RegisterUserServiceTest.createAppUser();
        appUserRepository.saveAndFlush(user);
        String username = user.getUsername() + "non-existing";
        AppUser actual = userPersistenceAdapter.findUserByUsername(username);
        assertNull(actual);
    }

    @Test
    public void findUserByUsername_whenUsernameExistsAlready_shouldReturnTheUser() {
        AppUser user = RegisterUserServiceTest.createAppUser();
        appUserRepository.saveAndFlush(user);
        String username = user.getUsername();
        AppUser actual = userPersistenceAdapter.findUserByUsername(username);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(user, actual)
        );
    }

    @Test
    public void findUserByEmail_whenEmailDoesNotExist_shouldReturnNull() {
        AppUser user = RegisterUserServiceTest.createAppUser();
        appUserRepository.saveAndFlush(user);
        String email = user.getEmail() + "non-existing";
        AppUser actual = userPersistenceAdapter.findUserByEmail(email);
        assertNull(actual);
    }

    @Test
    public void findUserByEmail_whenEmailExistsAlready_shouldReturnTheUser() {
        AppUser user = RegisterUserServiceTest.createAppUser();
        appUserRepository.saveAndFlush(user);
        String email = user.getEmail();
        AppUser actual = userPersistenceAdapter.findUserByEmail(email);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(user, actual)
        );
    }

    @Test
    public void findUserByLogin_whenLoginIsEqualToEmail_shouldReturnTheUser() {
        AppUser user = RegisterUserServiceTest.createAppUser();
        user.setUsername(null);

        appUserRepository.saveAndFlush(user);
        String login = user.getEmail();
        AppUser actual = userPersistenceAdapter.findUserByLogin(login);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(login, actual.getLogin()),
                () -> assertEquals(login, actual.getEmail())
        );
    }

    @Test
    public void findUserByLogin_whenLoginIsEqualToUsername_shouldReturnTheUser() {
        AppUser user = RegisterUserServiceTest.createAppUser();
        appUserRepository.saveAndFlush(user);
        String login = user.getUsername();
        AppUser actual = userPersistenceAdapter.findUserByLogin(login);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(login, actual.getLogin()),
                () -> assertEquals(login, actual.getUsername())
        );
    }

    @Test
    public void findUserByLogin_whenLoginIsNotEqualToUsernameNeitherToEmail_shouldReturnNull() {
        AppUser user = RegisterUserServiceTest.createAppUser();
        appUserRepository.saveAndFlush(user);
        String login = user.getEmail() + user.getUsername();
        AppUser actual = userPersistenceAdapter.findUserByLogin(login);

        assertNull(actual);
    }

    @Test
    public void saveUser_whenNameIsNull_shouldThrowException() {
        AppUser appUser = RegisterUserServiceTest.createAppUser();
        appUser.setName(null);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> userPersistenceAdapter.saveUser(appUser)
        );
    }

    @Test
    public void saveUser_whenPasswordIsNull_shouldThrowException() {
        AppUser appUser = RegisterUserServiceTest.createAppUser();
        appUser.setPassword(null);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> userPersistenceAdapter.saveUser(appUser)
        );
    }

    @Test
    public void saveUser_whenUserStateIsValid_shouldPersistIt() {
        AppUser appUser = RegisterUserServiceTest.createAppUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        AppUser persistedUser = userPersistenceAdapter.saveUser(appUser);
        assertEquals(appUser, persistedUser);
    }
}
