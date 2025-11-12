package com.library_book.admin_service.dto;

import java.util.Set;

public record AdminDetailsResponse(
        String id, String username, String passwordHash, Set<String> roles
) {}
