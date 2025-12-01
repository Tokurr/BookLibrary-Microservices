package com.library_book.library_service.controller;

import com.library_book.library_service.dto.AddBookRequest;
import com.library_book.library_service.dto.LibraryDto;
import com.library_book.library_service.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {

    private final LibraryService libraryService;


    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }


    @GetMapping("/list")
    public ResponseEntity<List<LibraryDto>> getAllLibraries()
    {
        return ResponseEntity.ok(libraryService.getAllLibraries());
    }

    @GetMapping("{libraryId}")
    public ResponseEntity<LibraryDto> getLibraryById(@PathVariable String libraryId)
    {
        return ResponseEntity.ok(libraryService.getAllBooksLibraryById(libraryId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<LibraryDto> createLibrary(@RequestParam String categoryName)
    {
        return ResponseEntity.ok(libraryService.createLibrary(categoryName));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
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


    @DeleteMapping("/remove/library/{libraryId}")
    public ResponseEntity<Void> removeLibrary(@PathVariable String libraryId)
    {
        libraryService.removeLibrary(libraryId);
        return ResponseEntity.noContent().build();

    }



}
