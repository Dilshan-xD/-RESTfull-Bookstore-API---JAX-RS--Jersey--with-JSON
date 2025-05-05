package com.bookstore.repositories;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.models.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BookRepository {
    private static BookRepository instance;
    private final Map<Long, Book> books;
    private final AtomicLong idCounter;

    private BookRepository() {
        this.books = new HashMap<>();
        this.idCounter = new AtomicLong(1);
        // Initialize with some sample data
        addSampleData();
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    private void addSampleData() {
        Book book1 = new Book(idCounter.getAndIncrement(), "The Great Gatsby", 1L, "978-0743273565", 1925, 12.99, 50);
        Book book2 = new Book(idCounter.getAndIncrement(), "To Kill a Mockingbird", 2L, "978-0061120084", 1960, 14.99, 75);
        Book book3 = new Book(idCounter.getAndIncrement(), "1984", 3L, "978-0451524935", 1949, 11.99, 60);
        
        books.put(book1.getId(), book1);
        books.put(book2.getId(), book2);
        books.put(book3.getId(), book3);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public Book getBookById(Long id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    public List<Book> getBooksByAuthorId(Long authorId) {
        List<Book> authorBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId().equals(authorId)) {
                authorBooks.add(book);
            }
        }
        return authorBooks;
    }

    public Book createBook(Book book) {
        book.setId(idCounter.getAndIncrement());
        books.put(book.getId(), book);
        return book;
    }

    public Book updateBook(Long id, Book updatedBook) {
        if (!books.containsKey(id)) {
            throw new BookNotFoundException(id);
        }
        updatedBook.setId(id);
        books.put(id, updatedBook);
        return updatedBook;
    }

    public void deleteBook(Long id) {
        if (!books.containsKey(id)) {
            throw new BookNotFoundException(id);
        }
        books.remove(id);
    }

    public boolean exists(Long id) {
        return books.containsKey(id);
    }

    public void updateStock(Long id, int quantity) {
        Book book = getBookById(id);
        book.setStock(book.getStock() - quantity);
        books.put(id, book);
    }
}

