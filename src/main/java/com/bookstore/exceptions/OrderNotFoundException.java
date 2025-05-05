package com.bookstore.exceptions;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(String message) {
        super(message);
    }
    
    public OrderNotFoundException(Long id) {
        super("Order with ID " + id + " does not exist.");
    }
}
