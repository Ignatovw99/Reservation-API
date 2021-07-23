package com.reservation.persistence.core.repository;

import com.reservation.persistence.core.entity.DomainEntityPropertyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainEntityPropertyJpaRepository extends JpaRepository<DomainEntityPropertyJpaEntity, Long> {

}
