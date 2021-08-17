package com.reservation.application.domain.entity;

import com.reservation.common.base.NumericJpaIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppRole extends NumericJpaIdentifier {

    private String name;
}
