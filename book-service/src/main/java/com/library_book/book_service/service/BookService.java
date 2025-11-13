package com.library_book.book_service.service;

import com.library_book.book_service.client.LibraryServiceClient;
import com.library_book.book_service.dto.BookDto;
import com.library_book.book_service.dto.BookIdDto;
import com.library_book.book_service.dto.CreateBookRequest;
import com.library_book.book_service.dto.convertor.BookDtoConvertor;
import com.library_book.book_service.exception.BookNotFoundException;
import com.library_book.book_service.model.Book;
import com.library_book.book_service.model.Location;
import com.library_book.book_service.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookDtoConvertor bookDtoConvertor;


    private final LibraryServiceClient librayServiceClient;

    public BookService(BookRepository bookRepository, BookDtoConvertor bookDtoConvertor, LibraryServiceClient librayServiceClient) {
        this.bookRepository = bookRepository;

        this.bookDtoConvertor = bookDtoConvertor;
        this.librayServiceClient = librayServiceClient;
    }

    public List<BookDto> getAllBooks()
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

    public BookDto createBook(CreateBookRequest createBookRequest, MultipartFile file)
    {
        byte[] imageBytes = null;

        try {
            if (file != null && !file.isEmpty()) {
                imageBytes = file.getBytes();
            }
        } catch (IOException e) {
            throw new RuntimeException("Resim okunamadı", e);
        }

        Location location = new Location(createBookRequest.getLocationDto().getFloor(),createBookRequest.getLocationDto().getSection(),createBookRequest.getLocationDto().getShelf(),createBookRequest.getLocationDto().getRoom());
        Book savedBook = new Book(createBookRequest.getTitle(),createBookRequest.getBookYear(),createBookRequest.getAuthor(),createBookRequest.getPressName(),createBookRequest.getIsbn(),createBookRequest.getCategory(),createBookRequest.getDescription(),imageBytes,location);
        return bookDtoConvertor.convertBookDto(bookRepository.save(savedBook));

    }

    @Transactional
    public void deleteBook(String id)
    {
        Book book = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException("Book couldn't found by id : " + id));

        bookRepository.delete(book);
        librayServiceClient.removeBookFromLibraries(id);

    }
    @Transactional(readOnly = true)
    public List<BookDto> searchBooks(String keyword, int page, int size)
    {

        Pageable pageable =  PageRequest.of(page, size);

        return bookRepository.searchBooks(keyword, pageable)
                .stream()
                .map(bookDtoConvertor::convertBookDto)
                .collect(Collectors.toList());
    }

}
