package com.bookstore.exceptions;

public class OutOfStockException extends RuntimeException {
    
    public OutOfStockException(String message) {
        super(message);
    }
    
    public OutOfStockException(Long bookId, int requested, int available) {
        super("Book with ID " + bookId + " has insufficient stock. Requested: " + requested + ", Available: " + available);
    }
}
