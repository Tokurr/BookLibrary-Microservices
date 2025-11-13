package com.library_book.book_service.dto;

import com.library_book.book_service.model.Location;
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
    private String category;
    private String description;
    private byte[] coverImage;
    LocationDto locationDto;

}
