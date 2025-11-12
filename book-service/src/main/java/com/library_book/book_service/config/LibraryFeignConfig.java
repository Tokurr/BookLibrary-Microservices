package com.library_book.book_service.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class LibraryFeignConfig {

    @Value("${internal.auth.header}")
    private String internalHeaderName;

    @Value("${internal.auth.token}")
    private String internalToken;

    @Bean
    public RequestInterceptor internalTokenHeaderInterceptor() {
        return template -> template.header(internalHeaderName, internalToken);
    }
}