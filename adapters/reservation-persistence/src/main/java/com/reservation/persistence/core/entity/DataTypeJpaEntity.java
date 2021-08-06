package com.reservation.persistence.core.entity;

import com.reservation.common.base.NumericJpaIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "data_types")
@Data
@EqualsAndHashCode(callSuper = true)
public class DataTypeJpaEntity extends NumericJpaIdentifier {

    @Column(name = "full_class_name")
    @NotNull
    private String fullClassName;
}
