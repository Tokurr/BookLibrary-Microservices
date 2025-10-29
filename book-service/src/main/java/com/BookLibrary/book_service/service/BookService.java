package com.BookLibrary.book_service.service;

import com.BookLibrary.book_service.dto.BookDto;
import com.BookLibrary.book_service.dto.BookIdDto;
import com.BookLibrary.book_service.dto.convertor.BookDtoConvertor;
import com.BookLibrary.book_service.exception.BookNotFoundException;
import com.BookLibrary.book_service.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookDtoConvertor bookDtoConvertor;


    public BookService(BookRepository bookRepository, BookDtoConvertor bookDtoConvertor) {
        this.bookRepository = bookRepository;

        this.bookDtoConvertor = bookDtoConvertor;
    }

    public List<BookDto> gelAllBooks()
    {
        return bookRepository.findAll()
                .stream()
                .map(bookDtoConvertor::convertBookDto)
                .collect(Collectors.toList());
    }


    public BookIdDto findByIsbn(String isbn)
    {
      return  bookDtoConvertor.convertBookIdDto(bookRepository.findByIsbn(isbn).orElseThrow(()-> new BookNotFoundException("Book couldn't found by isbn: " + isbn))) ;
    }

    public BookDto findBookDetailsById(String id)
    {
        return bookDtoConvertor.convertBookDto(bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException("Book couldn't found by id: " + id)));
    }

    

}
