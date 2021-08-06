package com.reservation.persistence.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.reservation.persistence"})
@EnableJpaRepositories(basePackages = {"com.reservation.persistence.core.repository"})
@EntityScan(basePackages = {"com.reservation.application.domain.entity", "com.reservation.persistence.core.entity"})
@EnableConfigurationProperties(LiquibaseProperties.class)
@AllArgsConstructor
@Setter
public class DatabaseConfiguration {

    private DataSource dataSource;

    private LiquibaseProperties properties;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new BeanAwareSpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(this.properties.getChangeLog());
        liquibase.setContexts(this.properties.getContexts());
        liquibase.setDefaultSchema(this.properties.getDefaultSchema());
        liquibase.setDropFirst(this.properties.isDropFirst());
        liquibase.setShouldRun(this.properties.isEnabled());
        liquibase.setLabels(this.properties.getLabels());
        liquibase.setChangeLogParameters(this.properties.getParameters());
        liquibase.setRollbackFile(this.properties.getRollbackFile());
        return liquibase;
    }
}
