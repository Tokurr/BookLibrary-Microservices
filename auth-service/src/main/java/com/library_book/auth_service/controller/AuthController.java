package com.library_book.auth_service.controller;

import com.library_book.auth_service.model.LoginRequest;
import com.library_book.auth_service.model.RefreshRequest;
import com.library_book.auth_service.model.TokenResponse;
import com.library_book.auth_service.service.AuthService;
import com.library_book.auth_service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request)
    {
        return ResponseEntity.ok(authService.refresh(request.username(), request.refreshToken()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header)
    {
        String token = header.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        authService.logout(token, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestParam String token) {
        Jws<Claims> jws = jwtUtil.parse(token);
        Claims c = jws.getBody();
        return ResponseEntity.ok(Map.of(
                "sub", c.getSubject(),
                "roles", c.get("roles"),
                "exp", c.getExpiration().getTime()
        ));
    }




}