package com.library.books.database.dao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name")
    String name;
    @ManyToOne
    Author author;
    @Column(name = "year")
    int year;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "BookAndCategory",
            joinColumns = @JoinColumn(name = "id_Book", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_Category", referencedColumnName = "id")
    )
    List<Category> categories = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
