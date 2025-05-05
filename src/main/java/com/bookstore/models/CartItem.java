package com.bookstore.models;

public class CartItem {
    private Long bookId;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Long bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
