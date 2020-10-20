package com.library.books.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    @Query("SELECT author FROM Author author")
    List<Author> getAll();

    @Query("SELECT author FROM Author author WHERE author.id=:id")
    Author getAuthorById(Long id);

    @Query("SELECT author FROM Author author WHERE author.fullName=:name")
    Author getAuthorByFullName(String name);
}
