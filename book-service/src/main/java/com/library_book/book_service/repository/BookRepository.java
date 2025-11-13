package com.library_book.book_service.repository;

import com.library_book.book_service.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,String> {

   Optional<Book> findByIsbn(String isbn);


   @Query("""
select b FROM Book b 
WHERE :keyword IS NULL 
or lower(b.title) LIKE lower(concat('%', :keyword, '%'))
or lower(b.author) LIKE lower(concat('%', :keyword, '%'))
""")
   List<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);




}
