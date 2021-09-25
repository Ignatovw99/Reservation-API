package com.reservation.application.service;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.in.AuthUseCase;
import com.reservation.application.port.out.FindUserPort;
import com.reservation.common.component.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Objects;

@UseCase
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final FindUserPort findUserPort;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        AppUser appUser = findUserPort.findUserByLogin(login);
        if (Objects.isNull(appUser)) {
            throw new UsernameNotFoundException(String.format(Constants.USERNAME_DOES_NOT_EXIST, login));
        }
//        TODO: get user authorities
        return new User(login, appUser.getPassword(), new ArrayList<>()); //Can use custom implementation of UserDetails
    }
}
