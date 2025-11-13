package com.library_book.book_service.dto.convertor;


import com.library_book.book_service.dto.BookDto;
import com.library_book.book_service.dto.BookIdDto;
import com.library_book.book_service.dto.LocationDto;
import com.library_book.book_service.model.Book;
import com.library_book.book_service.model.Location;
import org.springframework.stereotype.Component;

@Component
public class BookDtoConvertor {

    public BookDto convertBookDto(Book book)
    {
        LocationDto locationDto = new LocationDto(book.getLocation().getFloor(),book.getLocation().getSection(),book.getLocation().getShelf(),book.getLocation().getRoom());

        return new BookDto(book.getId(),book.getTitle(),book.getBookYear(),book.getAuthor(),book.getPressName(),book.getIsbn(),book.getCategory(),book.getDescription(),book.getCoverImage(),locationDto);
    }

    public BookIdDto convertBookIdDto(Book book)
    {
        return new BookIdDto(book.getId(),book.getIsbn());
    }


}
