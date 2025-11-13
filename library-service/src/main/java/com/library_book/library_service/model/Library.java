package com.library_book.library_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String categoryName;

    @ElementCollection
    private List<String> userBook = new ArrayList<>();

    public Library(String categoryName) {
        this.categoryName = categoryName;
    }
}
