package com.reservation.application.port.out;

import com.reservation.application.domain.entity.PropertyType;

public interface FindPropertyTypeByIdPort {

    PropertyType findById(Long id);
}
