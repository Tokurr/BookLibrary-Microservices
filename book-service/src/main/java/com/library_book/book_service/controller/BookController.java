package com.library_book.book_service.controller;


import com.library_book.book_service.dto.BookDto;
import com.library_book.book_service.dto.BookIdDto;
import com.library_book.book_service.dto.CreateBookRequest;
import com.library_book.book_service.dto.UpdateBook;
import com.library_book.book_service.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/book")
@Validated
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/list")
    public ResponseEntity<List<BookDto>> getAllBook(
            @RequestParam int size,
            @RequestParam int page
    )
    {
        return ResponseEntity.ok(bookService.getAllBooks(size,page));
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookDto> createBook(
          @Valid @RequestPart("book") CreateBookRequest request,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {

        return ResponseEntity.ok(bookService.createBook(request, coverImage));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id)
    {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooks(
            @RequestParam(required = false) String keyword,@RequestParam  int page, @RequestParam  int size

            ){

        return ResponseEntity.ok(bookService.searchBooks(keyword,page,size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<BookDto> updateBook(@RequestBody UpdateBook updateBook, @PathVariable String id)
    {
        return  ResponseEntity.ok(bookService.updateBook(updateBook,id));
    }



}
