package com.reservation.web.mapper;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.in.RegisterUserUseCase;
import com.reservation.common.contract.DomainEntityApiMapper;
import com.reservation.web.model.UserApi;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserApiMapper extends DomainEntityApiMapper<AppUser, UserApi> {

    RegisterUserUseCase.Command toRegisterUserCommand(UserApi userApi);
}
