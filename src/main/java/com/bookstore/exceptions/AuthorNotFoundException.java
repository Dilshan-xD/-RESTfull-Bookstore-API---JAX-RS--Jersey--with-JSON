package com.bookstore.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    
    public AuthorNotFoundException(String message) {
        super(message);
    }
    
    public AuthorNotFoundException(Long id) {
        super("Author with ID " + id + " does not exist.");
    }
}
