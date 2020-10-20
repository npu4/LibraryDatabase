package com.library.books.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("SELECT book FROM Book book")
    List<Book> getAll();

    @Query("SELECT book FROM Book book WHERE book.authorID=:authorID")
    List<Book> getAllByAuthorID(Long authorID);
}
