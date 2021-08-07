package com.reservation.web.model;

import com.reservation.common.base.NumericIdentifier;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class PropertyTypeApi extends NumericIdentifier {

    @EqualsAndHashCode.Include
    private String name;

    private Boolean requiresPrivate;

    private Boolean allowsMultipleRooms;

    private Boolean requiresAlternative;

    @Builder
    public PropertyTypeApi(final Long id, final String name, final Boolean requiresPrivate, final Boolean allowsMultipleRooms, final Boolean requiresAlternative) {
        super(id);
        this.name = name;
        this.requiresPrivate = requiresPrivate;
        this.allowsMultipleRooms = allowsMultipleRooms;
        this.requiresAlternative = requiresAlternative;
    }
}
