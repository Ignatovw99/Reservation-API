package com.reservation.common.component;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersistenceAdapterTest {

    @PersistenceAdapter(value = "customPersistenceAdapter")
    static class EntityPersistenceAdapter { }

    static class NonComponentPersistenceAdapter { }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(CustomComponentConfig.class);

    @Test
    public void testIfAnnotatedPersistenceAdapterIsRegisteredIntoAppContextAsComponent() {

        contextRunner
                .run(context -> assertAll(
                        () -> assertThat(context).hasSingleBean(PersistenceAdapterTest.EntityPersistenceAdapter.class),
                        () -> assertThat(context).doesNotHaveBean(PersistenceAdapterTest.NonComponentPersistenceAdapter.class)
                ));
    }

    @Test
    public void testIfPersistenceAdapterAnnotationHasComponentAnnotation() {
        Component subAnnotation = PersistenceAdapter.class.getAnnotation(Component.class);
        assertNotNull(subAnnotation);
    }

    @Test
    public void persistenceAdapterAnnotationValueAttribute_shouldRegisterComponentsWithTheGivenName() {
        contextRunner
                .run(context -> assertAll(
                        () -> assertThat(context).hasBean("customPersistenceAdapter"),
                        () -> assertThat(context).doesNotHaveBean("entityPersistenceAdapter"))
                );
    }
}
