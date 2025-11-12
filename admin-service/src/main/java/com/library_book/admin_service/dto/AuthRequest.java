package com.library_book.admin_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record  AuthRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
