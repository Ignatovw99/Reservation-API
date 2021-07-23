package com.reservation.application.domain.entity;

import com.reservation.application.domain.entity.base.DomainEntity;
import com.reservation.application.domain.entity.base.NumericIdentifier;

import java.time.LocalDate;
import java.util.Objects;

@DomainEntity
public class Property extends NumericIdentifier {

    private String name;

    private Byte stars;

    private PropertyType type;

    private Location location;

    private String email;

    private String telephoneNumber;

    private String description;

    private LocalDate openedIn;

    private LocalDate createdAt;

    private LocalDate offeredSince;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getStars() {
        return stars;
    }

    public void setStars(Byte stars) {
        this.stars = stars;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getOpenedIn() {
        return openedIn;
    }

    public void setOpenedIn(LocalDate openedIn) {
        this.openedIn = openedIn;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getOfferedSince() {
        return offeredSince;
    }

    public void setOfferedSince(LocalDate offeredSince) {
        this.offeredSince = offeredSince;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property = (Property) o;
        return Objects.equals(getName(), property.getName()) &&
                Objects.equals(getStars(), property.getStars()) &&
                Objects.equals(getType(), property.getType()) &&
                Objects.equals(getLocation(), property.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getStars(), getType(), getLocation());
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + super.getId() +
                "name='" + name + '\'' +
                ", stars=" + stars +
                ", email='" + email + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", openedIn=" + openedIn +
                ", createdAt=" + createdAt +
                ", offeredSince=" + offeredSince +
                '}';
    }
}
