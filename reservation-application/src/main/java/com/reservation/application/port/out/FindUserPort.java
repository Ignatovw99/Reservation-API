package com.reservation.application.port.out;

import com.reservation.application.domain.entity.AppUser;

public interface FindUserPort {

    AppUser findUserByUsername(String username);

    AppUser findUserByEmail(String email);

    AppUser findUserByLogin(String login);
}
