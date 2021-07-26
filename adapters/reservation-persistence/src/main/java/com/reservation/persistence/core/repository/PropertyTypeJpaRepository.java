package com.reservation.persistence.core.repository;

import com.reservation.application.domain.entity.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyTypeJpaRepository extends JpaRepository<PropertyType, Long> {

    PropertyType findByName(String name);
}
