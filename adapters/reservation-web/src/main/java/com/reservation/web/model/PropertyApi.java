package com.reservation.web.model;

import com.reservation.common.base.NumericIdentifier;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class PropertyApi extends NumericIdentifier {

    @EqualsAndHashCode.Include
    private String name;

    private Byte starRatings;

    private Long propertyTypeId;

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
    public PropertyApi(Long id,
                       String name,
                       Byte starRatings,
                       Long propertyTypeId,
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
        this.propertyTypeId = propertyTypeId;
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
