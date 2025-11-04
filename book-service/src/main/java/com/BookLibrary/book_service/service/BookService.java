package com.BookLibrary.book_service.service;

import com.BookLibrary.book_service.client.LibraryServiceClient;
import com.BookLibrary.book_service.dto.BookDto;
import com.BookLibrary.book_service.dto.BookIdDto;
import com.BookLibrary.book_service.dto.CreateBookRequest;
import com.BookLibrary.book_service.dto.convertor.BookDtoConvertor;
import com.BookLibrary.book_service.exception.BookNotFoundException;
import com.BookLibrary.book_service.model.Book;
import com.BookLibrary.book_service.repository.BookRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookDtoConvertor bookDtoConvertor;


    private final LibraryServiceClient librayServiceClient;

    public BookService(BookRepository bookRepository, BookDtoConvertor bookDtoConvertor, KafkaTemplate<String, Object> kafkaTemplate, LibraryServiceClient librayServiceClient) {
        this.bookRepository = bookRepository;

        this.bookDtoConvertor = bookDtoConvertor;
        this.librayServiceClient = librayServiceClient;
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

    public BookDto createBook(CreateBookRequest createBookRequest)
    {
        Book savedBook = new Book(createBookRequest.getTitle(),createBookRequest.getBookYear(),createBookRequest.getAuthor(),createBookRequest.getPressName(),createBookRequest.getIsbn());
        return bookDtoConvertor.convertBookDto(bookRepository.save(savedBook));

    }

    @Transactional
    public void deleteBook(String id)
    {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException("Book couldn't found by id : " + id));

        bookRepository.delete(book);
        librayServiceClient.removeBookFromLibraries(id);

    }

}
