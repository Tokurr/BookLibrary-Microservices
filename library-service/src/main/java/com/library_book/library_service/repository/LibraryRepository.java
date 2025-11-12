package com.library_book.library_service.repository;

import com.library_book.library_service.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository  extends JpaRepository<Library, String> {




}
