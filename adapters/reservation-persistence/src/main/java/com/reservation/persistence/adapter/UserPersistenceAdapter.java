package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.out.FindUserPort;
import com.reservation.application.port.out.PersistUserPort;
import com.reservation.common.component.PersistenceAdapter;
import com.reservation.persistence.core.repository.AppUserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
class UserPersistenceAdapter implements FindUserPort, PersistUserPort {

    private final AppUserJpaRepository appUserRepository;

    @Override
    public AppUser findUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser findUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return appUserRepository.findByEmail(email);
    }

    @Override
    public AppUser findUserByLogin(String login) {
        log.info("Fetching user by login: {}", login);
        return appUserRepository.findByUsernameOrEmail(login, login);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Persisting user: {}", user);
        return appUserRepository.saveAndFlush(user);
    }
}
