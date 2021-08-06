package com.reservation.persistence.core.entity;

import com.reservation.common.base.NumericJpaIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "domain_entities")
@Data
@EqualsAndHashCode(callSuper = true)
public class DomainEntityJpaEntity extends NumericJpaIdentifier {

    @Column(name = "name")
    @NotNull
    private String name;
}
