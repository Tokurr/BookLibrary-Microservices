package com.BookLibrary.library_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String id;
    private String title;
    private Integer year;
    private String author;
    private String pressName;
    private String Isbn;

    public BookDto(String id, String isbn) {
        this.id = id;
        Isbn = isbn;
    }
}
