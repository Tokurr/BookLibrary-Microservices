package com.library_book.admin_service.service;

import com.library_book.admin_service.dto.AdminCreateRequest;
import com.library_book.admin_service.dto.AdminDetailsResponse;
import com.library_book.admin_service.dto.AdminDto;
import com.library_book.admin_service.model.Admin;
import com.library_book.admin_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository repo;
    private final PasswordEncoder encoder;

    public AdminDto create(AdminCreateRequest req) {
        if (repo.existsByUsername(req.username()))
            throw new RuntimeException("Username already exists");
        Admin a = Admin.builder()
                .username(req.username())
                .passwordHash(encoder.encode(req.password()))
                .roles(Set.of("ADMIN"))
                .build();
        repo.save(a);
        return new AdminDto(a.getId(), a.getUsername());
    }

    public List<AdminDto> list() {
        return repo.findAll().stream()
                .map(a -> new AdminDto(a.getId(), a.getUsername()))
                .toList();
    }

    public AdminDetailsResponse internalByUsername(String username) {
        Admin a = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new AdminDetailsResponse(a.getId(), a.getUsername(), a.getPasswordHash(), a.getRoles());
    }
}