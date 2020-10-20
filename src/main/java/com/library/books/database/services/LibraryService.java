package com.library.books.database.services;

import com.library.books.database.dao.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
        //defaultFull();

        Long authorID = (long) new Random().nextInt(authorRepository.getAll().size()) + 1;
        System.out.println("Книги автора " + authorRepository.getAuthorById(authorID).getFullName() + ":");
        List<Book> booksByAuthor = getBooksByAuthor(authorRepository.getAuthorById(authorID).getFullName());
        for (Book book: booksByAuthor) {
            System.out.println(book.getName());
        }

        Integer categoryID = new Random().nextInt(categoryRepository.getAll().size()) + 1;
        String categoryName = categoryRepository.getCategoryById(categoryID).getName();
        System.out.println("Все книги из категории " + categoryName + ":");
        List<Book> booksByCategory = getBooksByCategory(categoryName);
        for (Book book: booksByCategory) {
            System.out.println(book.getName());
        }

        System.out.println("Авторы с книгой из категории " + categoryName + ":");
        List<Author> authorsWithBookInCategory = getAuthorsWithBookInCategory(categoryName);
        for (Author aut: authorsWithBookInCategory) {
            System.out.println(aut.getFullName());
        }

        System.out.println("Авторы без книг:");
        List<Author> authorsWithoutBooks = getAuthorsWithoutBooks();
        for (Author aut: authorsWithoutBooks) {
            System.out.println(aut.getFullName());
        }

        System.out.println("Авторы со словом \"ло\" в названии книги:");
        List<Author> authorsWithWordInBook = getAuthorsWithWordInBook("ло");
        for (Author aut: authorsWithWordInBook) {
            System.out.println(aut.getFullName());
        }
    }


    List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.getAllByAuthorID(authorRepository.getAuthorByFullName(authorName).getId());
    }

    List<Book> getBooksByCategory(String categoryName) {
        List<Book> books = bookRepository.getAll();
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
        List<Author> authors = authorRepository.getAll();
        List<Author> authorsWithoutBooks = new ArrayList<>();
        for (Author author : authors) {
            List<Book> books = bookRepository.getAllByAuthorID(author.getId());
            if (books.size() == 0) {
                authorsWithoutBooks.add(author);
            }
        }
        return authorsWithoutBooks;
    }

    List<Author> getAuthorsWithWordInBook(String word) {
        List<Author> authors = authorRepository.getAll();
        List<Author> authorsWithWordInBook = new ArrayList<>();
        for (Author author : authors) {
            List<Book> books = bookRepository.getAllByAuthorID(author.getId());
            for (Book book : books) {
                if (book.getName().contains(word)) {
                    authorsWithWordInBook.add(author);
                    break;
                }
            }
        }
        return authorsWithWordInBook;
    }

    List<Author> getAuthorsWithBookInCategory(String categoryName) {
        List<Author> authors = authorRepository.getAll();
        List<Author> authorsWithBookInCategory = new ArrayList<>();
        for (Author author : authors) {
            List<Book> books = bookRepository.getAllByAuthorID(author.getId());
            forBooks:
            {
                for (Book book : books) {
                    List<Category> categories = book.getCategories();
                    for (Category category : categories) {
                        if (category.getName().equals(categoryName)) {
                            authorsWithBookInCategory.add(author);
                            break forBooks;
                        }
                    }
                }
            }
        }
        return authorsWithBookInCategory;
    }

    void defaultFull() {
        String[] authorNames = {
                "Дюма Александр",
                "Дойл Адриан Конан",
                "Лермонтов Михаил Юрьевич",
                "Беляев Александр Романович"
        };
        for (String authorName : authorNames) {
            Author author = new Author();
            author.setFullName(authorName);
            authorRepository.save(author);
        }

        String[] categoryNames = {
                "исторический роман",
                "роман-фельетон",
                "роман плаща и шпаги",
                "криминальный рассказ",
                "детектив",
                "приключенческий роман",
                "научная фантастика"
        };
        for (String categoryName : categoryNames) {
            Category category = new Category();
            category.setName(categoryName);
            categoryRepository.save(category);
        }

        Collection<String[]> books = new ArrayList<>();
        books.add(new String[]{"Королева Марго",                "1995", "Дюма Александр",           "исторический роман"});
        books.add(new String[]{"Три мушкетера",                 "1987", "Дюма Александр",           "роман-фельетон", "роман плаща и шпаги"});
        books.add(new String[]{"Приключения Шерлока Холмса",    "2005", "Дойл Адриан Конан",        "криминальный рассказ", "детектив"});
        books.add(new String[]{"Граф Монте-Кристо",             "1999", "Дюма Александр",           "исторический роман", "приключенческий роман"});
        books.add(new String[]{"Голова профессора Доуэля",      "2010", "Беляев Александр Романович", "научная фантастика"});
        books.add(new String[]{"Человек-амфибия",               "2015", "Беляев Александр Романович", "научная фантастика"});
        for (String[] book : books) {
            Book newBook = new Book();
            newBook.setName(book[0]);
            newBook.setYear(Integer.parseInt(book[1]));
            newBook.setAuthorID(authorRepository.getAuthorByFullName(book[2]).getId());
            List<Category> categories = new ArrayList<>();
            for (int i = 3; i < book.length; i++) {
                categories.add(categoryRepository.getCategoryByName(book[i]));
            }
            newBook.setCategories(categories);
            bookRepository.save(newBook);
        }
    }
}
