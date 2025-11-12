package com.library_book.auth_service.model;

public record RefreshRequest(String username, String refreshToken) {}
