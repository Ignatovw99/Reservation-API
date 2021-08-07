package com.reservation.application.domain.entity;

import com.reservation.application.domain.entity.base.DomainEntity;
import com.reservation.common.base.NumericJpaIdentifier;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@DomainEntity
@Entity
@Table(name = "property_type")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class PropertyType extends NumericJpaIdentifier {

    @EqualsAndHashCode.Include
    private String name;

    private Boolean requiresPrivate;

    private Boolean allowsMultipleRooms;

    private Boolean requiresAlternative;

    @Builder
    public PropertyType(final Long id,
                        final String name,
                        final Boolean requiresPrivate,
                        final Boolean allowsMultipleRooms,
                        final Boolean requiresAlternative) {
        super(id);
        this.name = name;
        this.requiresPrivate = requiresPrivate;
        this.allowsMultipleRooms = allowsMultipleRooms;
        this.requiresAlternative = requiresAlternative;
    }
}
