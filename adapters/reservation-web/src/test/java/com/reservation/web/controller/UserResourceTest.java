package com.reservation.web.controller;

import com.reservation.application.domain.entity.AppUser;
import com.reservation.application.port.in.RegisterUserUseCase;
import com.reservation.application.service.RegisterUserServiceTest;
import com.reservation.web.mapper.UserApiMapper;
import com.reservation.web.model.UserApi;
import com.reservation.web.model.error.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserResource.class)
public class UserResourceTest {

    @Autowired
    private UserResource userResource;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private UserApiMapper userApiMapper;

    public static UserApi createUserApi() {
        return UserApi.builder()
                    .username(RegisterUserServiceTest.USERNAME)
                    .name(RegisterUserServiceTest.NAME)
                    .email(RegisterUserServiceTest.EMAIL)
                    .password(RegisterUserServiceTest.PASSWORD)
                .build();
    }

    @Test
    public void registerUser_whenTheRequestIdIsNotNull_shouldThrowException() {
        UserApi request = createUserApi();
        request.setId(13L);

        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> userResource.registerUser(request)
        );

        assertThat(exception).hasMessage("Request failed, request model should not have id");
    }

    @Test
    public void registerUser_whenUseCaseServiceRaisesAnError_shouldThrowAnException() {
        UserApi request = createUserApi();
        RegisterUserUseCase.Command command = RegisterUserServiceTest.createCommand();

        when(userApiMapper.toRegisterUserCommand(request))
                .thenReturn(command);
        when(registerUserUseCase.register(command))
                .thenThrow(RegisterUserUseCase.InvalidUserRegistrationException.class);

        assertThrows(
                RegisterUserUseCase.InvalidUserRegistrationException.class,
                () -> userResource.registerUser(request)
        );

        verify(registerUserUseCase).register(any(RegisterUserUseCase.Command.class));
    }

    @Test
    public void registerUser_whenRequestIsValid_shouldReturnTheNewUser() {
        final Long id = 32L;
        UserApi request = createUserApi();
        UserApi responseExpected = createUserApi();
        responseExpected.setId(id);

        RegisterUserUseCase.Command command = RegisterUserServiceTest.createCommand();
        AppUser appUser = RegisterUserServiceTest.createAppUser();

        when(userApiMapper.toRegisterUserCommand(request))
                .thenReturn(command);
        when(registerUserUseCase.register(command))
                .thenReturn(appUser);
        when(userApiMapper.toApiModel(appUser))
                .thenReturn(responseExpected);

        ResponseEntity<UserApi> response = userResource.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.LOCATION));
        UserApi responseBody = response.getBody();
        assertAll(
                () -> assertNotNull(responseBody),
                () -> assertEquals(id, responseBody.getId()),
                () -> assertEquals(responseExpected.getName(), responseBody.getName()),
                () -> assertEquals(responseExpected.getUsername(), responseBody.getUsername()),
                () -> assertEquals(responseExpected.getEmail(), responseBody.getEmail())
        );

        verify(userApiMapper).toRegisterUserCommand(request);
        verify(registerUserUseCase).register(any(RegisterUserUseCase.Command.class));
        verify(userApiMapper).toApiModel(appUser);
    }
}
