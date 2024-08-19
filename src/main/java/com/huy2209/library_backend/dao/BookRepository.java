package com.huy2209.library_backend.dao;

import com.huy2209.library_backend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingOrAuthorContaining(@Param("title") String title, @Param("author") String author, Pageable pageable);

    Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);

    @Query("SELECT b FROM Book b LEFT JOIN Review r ON b.id = r.bookId " +
            "GROUP BY b HAVING COUNT(r.id) > 0 ORDER BY AVG(r.rating) DESC")
    Page<Book> findTopRatedBooks(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE id in :book_ids")
    List<Book> findBookByBookIds(@Param("book_ids") List<Long> bookIds);

}
