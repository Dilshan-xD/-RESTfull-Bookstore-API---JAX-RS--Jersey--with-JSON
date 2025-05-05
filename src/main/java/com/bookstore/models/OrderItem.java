package com.bookstore.models;

public class OrderItem {
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private double unitPrice;

    public OrderItem() {
    }

    public OrderItem(Long bookId, String bookTitle, int quantity, double unitPrice) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return quantity * unitPrice;
    }
}
