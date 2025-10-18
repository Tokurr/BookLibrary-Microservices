package com.BookLibrary.book_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;


@Entity
@Table(name = "books")
@Data
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String bookYear;
    private String author;
    private String pressName;
    private String isbn;

}
