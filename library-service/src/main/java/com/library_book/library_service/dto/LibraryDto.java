package com.library_book.library_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryDto {
    private String id;
    private List<BookDto> userBookList;


}
