package com.reservation.persistence.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.ResourceLoader;

public class BeanAwareSpringLiquibase extends SpringLiquibase {

    private static ResourceLoader applicationContext;

    public static <T> T getBean(Class<T> beanClass) {
        if (ApplicationContext.class.isInstance(applicationContext)) {
            return ((ApplicationContext)applicationContext).getBean(beanClass);
        } else {
            throw new ApplicationContextException("Resource loader is not an instance of ApplicationContext");
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        super.setResourceLoader(resourceLoader);
        applicationContext = resourceLoader;
    }
}
