package com.reservation.persistence.core.repository;

import com.reservation.application.domain.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyJpaRepository extends JpaRepository<Property, Long> {

    Property findByName(String name);
}
