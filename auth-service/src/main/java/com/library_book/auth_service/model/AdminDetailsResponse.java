package com.library_book.auth_service.model;

import java.util.Set;

public record AdminDetailsResponse(
        String id,
        String username,
        String passwordHash,
        Set<String> roles
) {}