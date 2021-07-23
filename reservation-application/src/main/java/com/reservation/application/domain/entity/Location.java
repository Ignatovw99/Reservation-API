package com.reservation.application.domain.entity;

import com.reservation.application.domain.entity.base.DomainEntity;
import com.reservation.common.processor.DomainEntityProperty;
import com.reservation.application.domain.entity.base.NumericIdentifier;

import java.time.LocalDate;
import java.util.Objects;

@DomainEntity
public class Location extends NumericIdentifier {

    private String country;

    private String town;

    private String street;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(getCountry(), location.getCountry()) &&
                Objects.equals(getTown(), location.getTown()) &&
                Objects.equals(getStreet(), location.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getTown(), getStreet());
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + super.getId() +
                "country='" + country + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
