package com.library_book.book_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBook {

    private String title;
    private int bookYear;
    private String author;
    private String pressName;
    private String isbn;
    private String description;

}
