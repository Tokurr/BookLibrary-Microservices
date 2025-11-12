package com.library_book.admin_service.controller;


import com.library_book.admin_service.dto.AdminCreateRequest;
import com.library_book.admin_service.dto.AdminDetailsResponse;
import com.library_book.admin_service.dto.AdminDto;
import com.library_book.admin_service.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

    private final AdminService service;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<AdminDto> create(@Valid @RequestBody AdminCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<AdminDto>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("by-username/{username}")
    public ResponseEntity<AdminDetailsResponse> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.internalByUsername(username));
    }
}