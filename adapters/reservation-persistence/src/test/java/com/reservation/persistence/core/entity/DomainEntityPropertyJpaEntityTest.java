package com.reservation.persistence.core.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DomainEntityPropertyJpaEntityTest {

    @Test
    public void verifyEquals() {
        EqualsVerifier.forClass(DomainEntityPropertyJpaEntity.class)
                .withRedefinedSuperclass()
                .withOnlyTheseFields("id", "name", "domainEntity")
                .verify();
    }

    @Test
    public void hasDataType_whenNullIsGiven_shouldReturnFalse() {

        DataTypeJpaEntity dataTypeJpaEntity = new DataTypeJpaEntity();
        dataTypeJpaEntity.setFullClassName(String.class.getName());

        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = new DomainEntityPropertyJpaEntity();
        domainEntityPropertyJpaEntity.setDataType(dataTypeJpaEntity);

        boolean actualResult = domainEntityPropertyJpaEntity.hasDataType(null);

        assertFalse(actualResult);
    }

    @Test
    public void hasDataType_whenDifferentDataTypeIsGiven_shouldReturnFalse() {

        DataTypeJpaEntity dataTypeJpaEntity = new DataTypeJpaEntity();
        dataTypeJpaEntity.setFullClassName(String.class.getName());

        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = new DomainEntityPropertyJpaEntity();
        domainEntityPropertyJpaEntity.setDataType(dataTypeJpaEntity);

        boolean actualResult = domainEntityPropertyJpaEntity.hasDataType(Integer.class.getName());

        assertFalse(actualResult);
    }

    @Test
    public void hasDataType_whenTheSameDataTypeIsGiven_shouldReturnTrue() {

        DataTypeJpaEntity dataTypeJpaEntity = new DataTypeJpaEntity();
        dataTypeJpaEntity.setFullClassName(String.class.getName());

        DomainEntityPropertyJpaEntity domainEntityPropertyJpaEntity = new DomainEntityPropertyJpaEntity();
        domainEntityPropertyJpaEntity.setDataType(dataTypeJpaEntity);

        boolean actualResult = domainEntityPropertyJpaEntity.hasDataType(String.class.getName());

        assertTrue(actualResult);
    }
}
