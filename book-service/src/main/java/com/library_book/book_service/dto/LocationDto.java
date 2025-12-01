package com.library_book.book_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotBlank
    private String floor;
    @NotBlank
    private String section;
    @NotBlank
    private String shelf;
    @NotBlank
    private String room;
}
