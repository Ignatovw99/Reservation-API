package com.reservation.persistence.core.repository;

import com.reservation.persistence.core.entity.DomainEntityJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface DomainEntityJpaRepository extends JpaRepository<DomainEntityJpaEntity, Long> {

    DomainEntityJpaEntity findByName(@NotNull String name);
}
