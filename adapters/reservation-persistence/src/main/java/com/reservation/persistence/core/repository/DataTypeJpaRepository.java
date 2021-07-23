package com.reservation.persistence.core.repository;

import com.reservation.persistence.core.entity.DataTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface DataTypeJpaRepository extends JpaRepository<DataTypeJpaEntity, Long> {

    DataTypeJpaEntity findByFullClassNameEquals(@NotNull String fullClassName);
}
