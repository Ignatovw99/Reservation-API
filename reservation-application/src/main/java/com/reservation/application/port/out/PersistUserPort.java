package com.reservation.application.port.out;

import com.reservation.application.domain.entity.AppUser;

public interface PersistUserPort {

    AppUser saveUser(AppUser user);
}
