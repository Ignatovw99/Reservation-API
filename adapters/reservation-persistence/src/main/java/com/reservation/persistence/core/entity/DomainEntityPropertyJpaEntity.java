package com.reservation.persistence.core.entity;

import com.reservation.common.base.NumericJpaIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "domain_entity_properties")
@Data
@EqualsAndHashCode(callSuper = true)
public class DomainEntityPropertyJpaEntity extends NumericJpaIdentifier {

    @Column(name = "name")
    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_entity_id")
    @NotNull
    @ToString.Exclude
    private DomainEntityJpaEntity domainEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_type_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private DataTypeJpaEntity dataType;

    public boolean hasDataType(String dataTypeFullName) {
        return getDataType().getFullClassName()
                .equals(dataTypeFullName);
    }
}
