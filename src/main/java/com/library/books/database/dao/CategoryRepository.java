package com.library.books.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c")
    List<Category> getAll();

    Category getCategoryById(Integer id);

    @Query("SELECT c FROM Category c WHERE c.name=:name")
    Category getCategoryByName(String name);
}
