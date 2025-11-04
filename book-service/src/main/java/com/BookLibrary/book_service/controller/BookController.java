package com.BookLibrary.book_service.controller;


import com.BookLibrary.book_service.dto.BookDto;
import com.BookLibrary.book_service.dto.BookIdDto;
import com.BookLibrary.book_service.dto.CreateBookRequest;
import com.BookLibrary.book_service.service.BookService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/book")
@Validated
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBook()
    {
        return ResponseEntity.ok(bookService.gelAllBooks());
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookIdDto> getBookByIsbn(@PathVariable @NotEmpty String isbn)
    {
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable @NotEmpty String id) {
        return ResponseEntity.ok(bookService.findBookDetailsById(id));
    }

    @PostMapping()
    public ResponseEntity<BookDto> createBook(@RequestBody CreateBookRequest createBookRequest)
    {

        return ResponseEntity.ok(bookService.createBook(createBookRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id)
    {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();

    }



}
