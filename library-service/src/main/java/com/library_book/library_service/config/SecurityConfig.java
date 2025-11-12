package com.library_book.library_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final InternalServiceAuthFilter internalServiceAuthFilter;
    private final HeaderAuthFilter headerAuthFilter;

    public SecurityConfig(InternalServiceAuthFilter internalServiceAuthFilter, HeaderAuthFilter headerAuthFilter) {

        this.internalServiceAuthFilter = internalServiceAuthFilter;

        this.headerAuthFilter = headerAuthFilter;
    }

    @Bean
    SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/v1/library/create").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/v1/library/update").authenticated()

                        .anyRequest().permitAll()
                )
                .addFilterBefore(internalServiceAuthFilter ,AnonymousAuthenticationFilter.class)
                    .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class);;
        return http.build();
    }
}
