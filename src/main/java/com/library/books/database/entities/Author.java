package com.library.books.database.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "Author")
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String fullName;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    Collection<Book> book;

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Collection<Book> getBook() {
        return book;
    }

    public void setBook(Collection<Book> book) {
        this.book = book;
    }
}
