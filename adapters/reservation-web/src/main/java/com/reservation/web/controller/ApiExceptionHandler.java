package com.reservation.web.controller;

import com.reservation.application.port.in.CreatePropertyTypeUseCase;
import com.reservation.web.model.error.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(CreatePropertyTypeUseCase.NonUniquePropertyTypeNameException.class)
    protected ResponseEntity<ApiError> handlerNonUnique(CreatePropertyTypeUseCase.NonUniquePropertyTypeNameException ex) {

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
