package com.reservation.web.controller;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.in.RegisterUserUseCase;
import com.reservation.common.component.WebAdapter;
import com.reservation.web.mapper.UserApiMapper;
import com.reservation.web.model.UserApi;
import com.reservation.web.model.error.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@WebAdapter(valueURL = "/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final RegisterUserUseCase registerUserUseCase;

    private final UserApiMapper mapper;

    @PostMapping
    public ResponseEntity<UserApi> registerUser(@RequestBody UserApi userApi) {
        log.info("REST Http request: register user: {}", userApi.getName());

        if (Objects.nonNull(userApi.getId())) {
            log.error("Request failed, request model should not have id");
            throw new InvalidRequestException("Request failed, request model should not have id");
        }

        RegisterUserUseCase.Command registerUserCommand = mapper.toRegisterUserCommand(userApi);
        AppUser registeredUser = registerUserUseCase.register(registerUserCommand);
        UserApi result = mapper.toApiModel(registeredUser);

        return ApiController.postResponse(result);
    }
}
