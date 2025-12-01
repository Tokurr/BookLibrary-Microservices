package com.library_book.book_service.dto;

import com.library_book.book_service.model.Location;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {



    @NotBlank
    private String title;

    @Positive
    private int bookYear;
    @NotBlank(message = "author boş olamaz")
    private String author;
    @NotBlank
    private String pressName;
    @NotBlank
    private String isbn;
    @NotBlank
    private String category;
    @NotBlank
    private String description;
    private byte[] coverImage;
    LocationDto locationDto;

}
