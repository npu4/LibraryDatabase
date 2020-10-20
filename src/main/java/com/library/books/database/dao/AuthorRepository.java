package com.library.books.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    @Query("SELECT author FROM Author author WHERE author.fullName=:name")
    Author getAuthorByFullName(String name);

    @Query("SELECT author FROM Author author LEFT JOIN author.book as book WHERE book.id is NULL GROUP BY author.id")
    List<Author> getAllWithoutBooks();

    @Query("SELECT author FROM Author author JOIN author.book as book WHERE book.name LIKE CONCAT('%', :word, '%') GROUP BY author.id")
    List<Author> getAllWithWordInBook(String word);

    @Query("SELECT author FROM Author author JOIN author.book as book JOIN book.categories as categories WHERE categories.name=:categoryName GROUP BY author.id")
    List<Author> getAllWithBookInCategory(String categoryName);
}
