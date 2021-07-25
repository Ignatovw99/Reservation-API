package com.reservation.common.component;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebAdapterTest {

    @WebAdapter(valueURL = "/api", value = "customWebAdapter")
    static class EntityWebAdapter {

        @GetMapping("/test")
        public ResponseEntity<String> getHandler() {
            return ResponseEntity.ok("Get Handler");
        }
    }

    static class NonComponentWebAdapter { }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(CustomComponentConfig.class);

    @Test
    public void testIfAnnotatedWebAdapterIsRegisteredIntoAppContextAsComponent() {

        contextRunner
                .run(context -> assertAll(
                        () -> assertThat(context).hasSingleBean(WebAdapterTest.EntityWebAdapter.class),
                        () -> assertThat(context).doesNotHaveBean(WebAdapterTest.NonComponentWebAdapter.class)
                ));
    }

    @Test
    public void testIfWebAdapterAnnotationHasRestControllerAnnotation() {
        RestController subAnnotation = WebAdapter.class.getAnnotation(RestController.class);
        assertNotNull(subAnnotation);
    }

    @Test
    public void testIfWebAdapterAnnotationHasRequestMappingAnnotation() {
        RequestMapping subAnnotation = WebAdapter.class.getAnnotation(RequestMapping.class);
        assertNotNull(subAnnotation);
    }

    @Test
    public void webAdapterAnnotationValueAttribute_shouldRegisterComponentsWithTheGivenName() {
        contextRunner
                .run(context -> assertAll(
                        () -> assertThat(context).hasBean("customWebAdapter"),
                        () -> assertThat(context).doesNotHaveBean("entityWebAdapter"))
                );
    }
}
