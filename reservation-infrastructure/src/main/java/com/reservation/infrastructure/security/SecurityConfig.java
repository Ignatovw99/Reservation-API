package com.reservation.infrastructure.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.reservation.infrastructure.security.filter.JwtAuthenticationFilter;
import com.reservation.infrastructure.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity //This overrides the default auto-config global web security
@EnableGlobalMethodSecurity(prePostEnabled = true) // Provides AOP security on methods -> Method Security Expressions
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final SecurityProperties securityProperties;

    @Bean //-> Singleton bean instance
    protected Algorithm jwtAlgorithm() {
        final String secret = securityProperties.getJwt().getSecret();
        return Algorithm.HMAC256(secret.getBytes());
    }

    protected UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authenticationManagerBean(), securityProperties, jwtAlgorithm());
        jwtAuthenticationFilter.setAuthenticationUrl(securityProperties.getLogin().getUrl());
        return jwtAuthenticationFilter;
    }

    protected OncePerRequestFilter authorizationFilter() {
        return new JwtAuthorizationFilter(securityProperties, jwtAlgorithm());
    }

    protected AuthenticationEntryPoint unauthenticatedHandler() {
        return new JwtAuthenticationEntryPoint(securityProperties.getAuthenticationErrorHeader());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder); // If we don’t specify, it will use plain text as pass.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthenticatedHandler())
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
//                    .antMatchers("/api/property-types").permitAll()
                    .anyRequest().permitAll()
//                    .anyRequest().authenticated()
                    .and()
                .addFilter(authenticationFilter())
                .addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
