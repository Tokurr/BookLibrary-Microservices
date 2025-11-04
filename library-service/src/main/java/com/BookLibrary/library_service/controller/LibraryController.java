package com.BookLibrary.library_service.controller;

import com.BookLibrary.library_service.dto.AddBookRequest;
import com.BookLibrary.library_service.dto.LibraryDto;
import com.BookLibrary.library_service.service.LibraryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {

    private final LibraryService libraryService;


    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("{bookId}")
    public ResponseEntity<LibraryDto> getLibraryById(@PathVariable String bookId)
    {
        return ResponseEntity.ok(libraryService.getAllBooksLibraryById(bookId));
    }

    @PostMapping
    public ResponseEntity<LibraryDto> createLibrary()
    {
        return ResponseEntity.ok(libraryService.createLibrary());
    }

    @PutMapping
    public ResponseEntity<Void> addBookToLibrary(@RequestBody AddBookRequest request)
    {
        libraryService.addBookToLibrary(request);
        return  ResponseEntity.ok().build();
    }


    @DeleteMapping("/removeFromLibraries/{bookId}")
    public ResponseEntity<Void> removeBookFromLibraries(@PathVariable String bookId)
    {
        libraryService.removeBookFromLibraries(bookId);
        return ResponseEntity.noContent().build();
    }



}
