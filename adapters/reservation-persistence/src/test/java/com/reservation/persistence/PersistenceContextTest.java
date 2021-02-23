package com.reservation.persistence;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = DatabaseConfiguration.class)
@ActiveProfiles("persistence")
public @interface PersistenceContextTest {

}
