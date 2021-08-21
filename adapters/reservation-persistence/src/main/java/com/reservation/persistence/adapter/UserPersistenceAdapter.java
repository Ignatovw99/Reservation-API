package com.reservation.persistence.adapter;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.out.FindUserPort;
import com.reservation.application.port.out.PersistUserPort;
import com.reservation.common.component.PersistenceAdapter;
import com.reservation.persistence.core.repository.AppUserJpaRepository;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class UserPersistenceAdapter implements FindUserPort, PersistUserPort {

    private final AppUserJpaRepository appUserRepository;

    @Override
    public AppUser findUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser findUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        return appUserRepository.saveAndFlush(user);
    }
}
