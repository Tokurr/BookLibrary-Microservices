package com.library_book.library_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class InternalServiceAuthFilter extends OncePerRequestFilter {
    @Value("${internal.auth.header}")
    private String headerName;

    @Value("${internal.auth.token}")
    private String expectedToken;

    private static final Set<String> PROTECTED_PREFIXES = Set.of(
            "/v1/library/removeFromLibraries"
    );
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String path = request.getRequestURI();

        return PROTECTED_PREFIXES.stream().noneMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String provided = req.getHeader(headerName);

        if (!constantTimeEquals(nonNull(provided), nonNull(expectedToken))) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized service call");
            return;
        }
        chain.doFilter(req, res);
    }

    private static boolean constantTimeEquals(String a, String b) {
        byte[] aBytes = a.getBytes();
        byte[] bBytes = b.getBytes();
        int diff = aBytes.length ^ bBytes.length;
        for (int i = 0; i < Math.min(aBytes.length, bBytes.length); i++) {
            diff |= aBytes[i] ^ bBytes[i];
        }
        return diff == 0;
    }

    private static String nonNull(String s) { return s == null ? "" : s; }

}
