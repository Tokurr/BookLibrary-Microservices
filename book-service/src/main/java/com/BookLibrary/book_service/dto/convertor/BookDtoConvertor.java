package com.BookLibrary.book_service.dto.convertor;


import com.BookLibrary.book_service.dto.BookDto;
import com.BookLibrary.book_service.dto.BookIdDto;
import com.BookLibrary.book_service.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookDtoConvertor {

    public BookDto convertBookDto(Book book)
    {
        return new BookDto(book.getId(),book.getTitle(),book.getBookYear(),book.getAuthor(),book.getPressName(),book.getIsbn());
    }

    public BookIdDto convertBookIdDto(Book book)
    {
        return new BookIdDto(book.getId(),book.getIsbn());
    }


}
