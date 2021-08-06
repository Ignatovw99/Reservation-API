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
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
public class PropertyType extends NumericJpaIdentifier {

    @EqualsAndHashCode.Include
    private String name;

    private Boolean requiresPrivate;

    private Boolean allowsMultipleRooms;

    private Boolean requiresAlternative;
}
