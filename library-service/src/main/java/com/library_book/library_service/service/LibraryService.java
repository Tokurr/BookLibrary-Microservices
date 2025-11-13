package com.library_book.library_service.service;

import com.library_book.library_service.client.BookServiceClient;
import com.library_book.library_service.dto.AddBookRequest;
import com.library_book.library_service.dto.BookIdDto;
import com.library_book.library_service.dto.LibraryDto;
import com.library_book.library_service.exception.LibraryNotFoundException;
import com.library_book.library_service.model.Library;
import com.library_book.library_service.repository.LibraryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookServiceClient bookServiceClient;


    public LibraryService(LibraryRepository libraryRepository, BookServiceClient bookServiceClient) {
        this.libraryRepository = libraryRepository;
        this.bookServiceClient = bookServiceClient;
    }


    public LibraryDto getAllBooksLibraryById(String id)
    {
        Library library = libraryRepository.findById(id)
                .orElseThrow(()-> new LibraryNotFoundException("Library could not found by id:" + id));

        LibraryDto libraryDto = new LibraryDto(library.getId(),
                library.getUserBook()
                        .stream()
                        .map(bookServiceClient::getBookById)
                        .map(ResponseEntity::getBody)
                        .collect(Collectors.toList()));
        return libraryDto;

    }


    public LibraryDto createLibrary(String categpryName)
    {
        Library library = new Library(categpryName);
        Library createLibrary = libraryRepository.save(library);
        return new LibraryDto(createLibrary.getId(),new ArrayList<>());

    }

    public void addBookToLibrary(AddBookRequest request)
    {
        BookIdDto book = bookServiceClient.getBookByIsbn(request.getIsbn()).getBody();

        Library library = libraryRepository.findById(request.getId()).orElseThrow(()->
                new LibraryNotFoundException("Library could not found "));


        library.getUserBook().add(book.getId());
        libraryRepository.save(library);
    }


    public void removeBookFromLibraries(String bookId){
        libraryRepository.findAll().forEach(library -> {
            if(library.getUserBook().remove(bookId))
            {
                libraryRepository.save(library);
            }
        });
    }

}
