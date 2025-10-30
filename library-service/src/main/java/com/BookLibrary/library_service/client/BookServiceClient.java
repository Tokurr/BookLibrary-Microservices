package com.BookLibrary.library_service.client;

import com.BookLibrary.library_service.dto.BookDto;
import com.BookLibrary.library_service.dto.BookIdDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@FeignClient(name = "book-service", path = "/v1/book")
public interface BookServiceClient {
    Logger logger = LoggerFactory.getLogger(BookServiceClient.class);

    @GetMapping("/isbn/{isbn}")
    @CircuitBreaker(name = "getBookByIsbnCircuitBreaker", fallbackMethod = "getBookFallback")
    ResponseEntity<BookIdDto> getBookByIsbn(@PathVariable(value = "isbn") String isbn);

    default ResponseEntity<BookIdDto> getBookFallback(String isbn, Exception exception) {

        logger.info("Book not found by isbn " + isbn + ", returning default BookDto object");

        return ResponseEntity.ok(new BookIdDto("default-book", "default-isbn"));
    }

    @GetMapping("/book/{bookId}")
    @CircuitBreaker(name = "getBookByIdCircuitBreaker", fallbackMethod = "getBookByIdFallback")
    ResponseEntity<BookDto> getBookById(@PathVariable("bookId") String bookId);

    default ResponseEntity<BookDto> getBookByIdFallback(String bookId, Throwable throwable) {
        logger.info("Book not found by id " + bookId + ", returning default BookDto object");
        return ResponseEntity.ok(new BookDto("defult-book","isbn"));
    }


}
