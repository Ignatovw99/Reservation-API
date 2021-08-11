package com.reservation.application.port.out;

import com.reservation.application.domain.entity.Property;

public interface PersistPropertyPort {

    Property saveProperty(Property property);
}
