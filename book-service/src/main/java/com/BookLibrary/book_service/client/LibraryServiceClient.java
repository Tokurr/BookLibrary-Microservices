package com.BookLibrary.book_service.client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "library-service", path = "/v1/library")
public interface LibraryServiceClient {
    Logger logger = LoggerFactory.getLogger(LibraryServiceClient.class);

    @DeleteMapping("/removeFromLibraries/{bookId}")
    @CircuitBreaker(name = "removeBookCircuitBreaker", fallbackMethod = "removeBookFromLibrariesFallback")
    void removeBookFromLibraries(@PathVariable String bookId);
    default ResponseEntity<Void> removeBookFromLibrariesFallback(String bookId, Throwable throwable) {
        logger.warn("⚠️ Library-service unreachable. Could not remove book '{}' from libraries. Cause: {}",
                bookId, throwable.getMessage());
        return ResponseEntity.accepted().build();
    }
}
