package com.library_book.book_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookIdDto {
    private String id;
    private String isbn;

}
