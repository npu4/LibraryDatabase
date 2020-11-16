package com.library.books.database.services;

import com.library.books.database.dao.*;
import com.library.books.database.entities.Author;
import com.library.books.database.entities.Book;
import com.library.books.database.entities.Category;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class LibraryService {
    AuthorRepository authorRepository;
    BookRepository bookRepository;
    CategoryRepository categoryRepository;

    public LibraryService(AuthorRepository authorRepository, BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @PostConstruct
    void init() {
        defaultFull();

        Long authorID = (long) new Random().nextInt(authorRepository.findAll().size()) + 1;
        if(authorRepository.findById(authorID).isPresent()){
            System.out.println("\n\nКниги автора " + authorRepository.findById(authorID).get().getFullName() + ":");
            List<Book> booksByAuthor = getBooksByAuthor(authorRepository.findById(authorID).get().getFullName());
            for (Book book : booksByAuthor) {
                System.out.println(book.getName());
            }
        }


        Integer categoryID = new Random().nextInt(categoryRepository.findAll().size()) + 1;
        if(categoryRepository.findById(categoryID).isPresent()){
            String categoryName = categoryRepository.findById(categoryID).get().getName();
            System.out.println("\n\nВсе книги из категории " + categoryName + ":");
            List<Book> booksByCategory = getBooksByCategory(categoryName);
            for (Book book : booksByCategory) {
                System.out.println(book.getName());
            }


            System.out.println("\n\nАвторы с книгой из категории " + categoryName + ":");
            List<Author> authorsWithBookInCategory = getAuthorsWithBookInCategory(categoryName);
            for (Author aut : authorsWithBookInCategory) {
                System.out.println(aut.getFullName());
            }
        }


        System.out.println("\n\nАвторы со словом \"ло\" в названии книги:");
        List<Author> authorsWithWordInBook = getAuthorsWithWordInBook("ло");
        for (Author aut : authorsWithWordInBook) {
            System.out.println(aut.getFullName());
        }
    }


    List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.getAllByAuthorID(authorRepository.getAuthorByFullName(authorName).getId());
    }

    List<Book> getBooksByCategory(String categoryName) {
        List<Book> books = bookRepository.findAll();
        List<Book> booksByCategory = new ArrayList<>();
        for (Book book : books) {
            List<Category> categories = book.getCategories();
            for (Category category : categories) {
                if (category.getName().equals(categoryName)) {
                    booksByCategory.add(book);
                    break;
                }
            }
        }
        return booksByCategory;
    }

    List<Author> getAuthorsWithoutBooks() {
        return authorRepository.getAllWithoutBooks();
    }

    List<Author> getAuthorsWithWordInBook(String word) {
        return authorRepository.getAllWithWordInBook(word);
    }

    List<Author> getAuthorsWithBookInCategory(String categoryName) {
        return authorRepository.getAllWithBookInCategory(categoryName);
    }

    void defaultFull() {
        Author author1 = new Author();
        author1.setFullName("Дюма Александр");
        Collection<String[]> books1 = new ArrayList<>();
        books1.add(new String[]{"Королева Марго", "1995", "исторический роман"});
        books1.add(new String[]{"Три мушкетера", "1987", "роман-фельетон", "роман плаща и шпаги"});
        books1.add(new String[]{"Граф Монте-Кристо", "1999", "исторический роман", "приключенческий роман"});
        addBooks(author1, books1);
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setFullName("Беляев Александр Романович");
        Collection<String[]> books2 = new ArrayList<>();
        books2.add(new String[]{"Человек-амфибия", "2015", "научная фантастика"});
        books2.add(new String[]{"Голова профессора Доуэля", "2010", "научная фантастика"});
        addBooks(author2, books2);
        authorRepository.save(author2);
    }

    void addBooks(Author author, Collection<String[]> books){
        Collection<Book> authorBooks = new ArrayList<>();
        for (String[] book : books) {
            Book newBook = new Book();
            newBook.setName(book[0]);
            newBook.setYear(Integer.parseInt(book[1]));
            List<Category> categories = new ArrayList<>();
            for (int i = 2; i < book.length; i++) {
                Category category = new Category();
                if (categoryRepository.getCategoryByName(book[i]) == null) {
                    category.setName(book[i]);
                }
                else {
                    category = categoryRepository.getCategoryByName(book[i]);
                }
                categories.add(category);
            }
            newBook.setCategories(categories);
            newBook.setAuthor(author);
            authorBooks.add(newBook);
            author.setBook(authorBooks);
        }
    }
}
