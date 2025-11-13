package com.library_book.book_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "books")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private int bookYear;
    private String author;
    private String pressName;
    private String isbn;

    private String category;

    private String description;

    @Lob
    private byte[] coverImage;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    public Book(String title, int bookYear, String author, String pressName, String isbn, String category, String description, byte[] coverImage, Location location) {
        this.title = title;
        this.bookYear = bookYear;
        this.author = author;
        this.pressName = pressName;
        this.isbn = isbn;
        this.category = category;
        this.description = description;
        this.coverImage = coverImage;
        this.location = location;
    }
}
