package com.reservation.persistence;

import com.reservation.persistence.config.DatabaseConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DataJpaTest
@ContextConfiguration(classes = DatabaseConfiguration.class)
@ActiveProfiles("persistence")
public @interface PersistenceContextTest {

    @AliasFor(annotation = DataJpaTest.class)
    String[] properties() default {};
}
