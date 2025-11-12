package com.library_book.admin_service.dto;

import jakarta.validation.constraints.NotBlank;

public record  AdminCreateRequest (
        @NotBlank String username,
        @NotBlank String password)
{ }
