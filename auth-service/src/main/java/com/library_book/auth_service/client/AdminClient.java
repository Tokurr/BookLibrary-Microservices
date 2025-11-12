package com.library_book.auth_service.client;

import com.library_book.auth_service.config.AuthFeignConfig;
import com.library_book.auth_service.model.AdminDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "admin-service",configuration = AuthFeignConfig.class)
public interface AdminClient {
    @GetMapping("/v1/admin/by-username/{username}")
    AdminDetailsResponse getByUsername(@PathVariable String username);
}
