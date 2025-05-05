package com.bookstore.repositories;

import com.bookstore.exceptions.AuthorNotFoundException;
import com.bookstore.models.Author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorRepository {
    private static AuthorRepository instance;
    private final Map<Long, Author> authors;
    private final AtomicLong idCounter;

    private AuthorRepository() {
        this.authors = new HashMap<>();
        this.idCounter = new AtomicLong(1);
        // Initialize with some sample data
        addSampleData();
    }

    public static synchronized AuthorRepository getInstance() {
        if (instance == null) {
            instance = new AuthorRepository();
        }
        return instance;
    }

    private void addSampleData() {
        Author author1 = new Author(idCounter.getAndIncrement(), "F. Scott Fitzgerald", "American novelist and short story writer.");
        Author author2 = new Author(idCounter.getAndIncrement(), "Harper Lee", "American novelist widely known for her novel To Kill a Mockingbird.");
        Author author3 = new Author(idCounter.getAndIncrement(), "George Orwell", "English novelist, essayist, journalist, and critic.");
        
        authors.put(author1.getId(), author1);
        authors.put(author2.getId(), author2);
        authors.put(author3.getId(), author3);
    }

    public List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    public Author getAuthorById(Long id) {
        Author author = authors.get(id);
        if (author == null) {
            throw new AuthorNotFoundException(id);
        }
        return author;
    }

    public Author createAuthor(Author author) {
        author.setId(idCounter.getAndIncrement());
        authors.put(author.getId(), author);
        return author;
    }

    public Author updateAuthor(Long id, Author updatedAuthor) {
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException(id);
        }
        updatedAuthor.setId(id);
        // Preserve the book IDs
        updatedAuthor.setBookIds(authors.get(id).getBookIds());
        authors.put(id, updatedAuthor);
        return updatedAuthor;
    }

    public void deleteAuthor(Long id) {
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException(id);
        }
        authors.remove(id);
    }

    public boolean exists(Long id) {
        return authors.containsKey(id);
    }

    public void addBookToAuthor(Long authorId, Long bookId) {
        Author author = getAuthorById(authorId);
        author.addBookId(bookId);
    }

    public void removeBookFromAuthor(Long authorId, Long bookId) {
        Author author = getAuthorById(authorId);
        author.removeBookId(bookId);
    }
}
