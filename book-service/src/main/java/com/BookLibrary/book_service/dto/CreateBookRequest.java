package com.BookLibrary.book_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

    private String title;
    private int bookYear;
    private String author;
    private String pressName;
    private String isbn;

}
