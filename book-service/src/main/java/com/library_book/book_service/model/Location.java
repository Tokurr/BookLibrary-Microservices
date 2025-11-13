package com.library_book.book_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String floor;
    private String section;
    private String shelf;
    private String room;

    public Location(String floor, String section, String shelf, String room) {
        this.floor = floor;
        this.section = section;
        this.shelf = shelf;
        this.room = room;
    }
}
