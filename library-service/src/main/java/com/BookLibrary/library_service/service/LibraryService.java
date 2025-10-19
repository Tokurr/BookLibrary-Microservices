package com.BookLibrary.library_service.service;

import com.BookLibrary.library_service.dto.LibraryDto;
import com.BookLibrary.library_service.exception.LibraryNotFoundException;
import com.BookLibrary.library_service.model.Library;
import com.BookLibrary.library_service.repository.LibraryRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;


    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }


    public LibraryDto getAllBooksLibraryById(String id)
    {
        Library library = libraryRepository.findById(id)
                .orElseThrow(()-> new LibraryNotFoundException("Library could not found by id:" + id));

        LibraryDto libraryDto = new LibraryDto(library.getId());
        return libraryDto;

    }


    public LibraryDto createLibrary()
    {
        Library createLibrary = libraryRepository.save(new Library());
        return new LibraryDto(createLibrary.getId());

    }


}
