package com.reservation.common.component;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UseCaseTest {

    @UseCase(value = "customUseCase")
    static class CreateDomainEntityUseCase { }

    static class NonComponentUseCase { }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(CustomComponentConfig.class);

    @Test
    public void testIfAnnotatedUseCaseIsRegisteredIntoAppContextAsComponent() {

        contextRunner
                .run(context -> assertAll(
                        () -> assertThat(context).hasSingleBean(CreateDomainEntityUseCase.class),
                        () -> assertThat(context).doesNotHaveBean(NonComponentUseCase.class)
                ));
    }

    @Test
    public void testIfUseCaseAnnotationHasComponentAnnotation() {
        Component subAnnotation = UseCase.class.getAnnotation(Component.class);
        assertNotNull(subAnnotation);
    }

    @Test
    public void useCaseAnnotationValueAttribute_shouldRegisterComponentsWithTheGivenName() {
        contextRunner
                .run(context -> assertAll(
                        () -> assertThat(context).hasBean("customUseCase"),
                        () -> assertThat(context).doesNotHaveBean("createDomainEntityUseCase"))
                );
    }
}
