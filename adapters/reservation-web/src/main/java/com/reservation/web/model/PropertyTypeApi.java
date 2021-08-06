package com.reservation.web.model;

import com.reservation.common.base.NumericIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class PropertyTypeApi extends NumericIdentifier {

    @EqualsAndHashCode.Include
    private String name;

    private Boolean requiresPrivate;

    private Boolean allowsMultipleRooms;

    private Boolean requiresAlternative;
}
