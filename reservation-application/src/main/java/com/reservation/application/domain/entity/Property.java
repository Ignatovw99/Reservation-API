package com.reservation.application.domain.entity;

import com.reservation.application.domain.entity.base.DomainEntity;
import com.reservation.common.base.NumericJpaIdentifier;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@DomainEntity
@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Property extends NumericJpaIdentifier {

    @EqualsAndHashCode.Include
    private String name;

    private Byte starRatings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_type_id")
    private PropertyType type;

    private String description;

    private String country;

    private String region;

    private String street;

    private Integer houseNumber;

    private String zip;

    private String contactName;

    private String contactNumber;

    private String contactEmail;

    @Builder
    public Property(Long id,
                    String name,
                    Byte starRatings,
                    PropertyType type,
                    String description,
                    String country,
                    String region,
                    String street,
                    Integer houseNumber,
                    String zip,
                    String contactName,
                    String contactNumber,
                    String contactEmail) {
        super(id);
        this.name = name;
        this.starRatings = starRatings;
        this.type = type;
        this.description = description;
        this.country = country;
        this.region = region;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
    }
}
