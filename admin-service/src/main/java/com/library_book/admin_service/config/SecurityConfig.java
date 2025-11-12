package com.library_book.admin_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final HeaderAuthFilter headerAuthFilter;
    private final InternalServiceAuthFilter internalServiceAuthFilter;
    public SecurityConfig(HeaderAuthFilter headerAuthFilter, InternalServiceAuthFilter internalServiceAuthFilter) {
        this.headerAuthFilter = headerAuthFilter;
        this.internalServiceAuthFilter = internalServiceAuthFilter;
    }

    @Bean
    SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/v1/admin/by-username/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(internalServiceAuthFilter , AnonymousAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}