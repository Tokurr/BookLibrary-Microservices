package com.BookLibrary.book_service.repository;

import com.BookLibrary.book_service.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,String> {

   Optional<Book> findByIsbn(String isbn);

}
