package com.bookstore.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(String message) {
        super(message);
    }
    
    public CustomerNotFoundException(Long id) {
        super("Customer with ID " + id + " does not exist.");
    }
}
