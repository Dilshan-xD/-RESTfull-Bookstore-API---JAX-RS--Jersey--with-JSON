package com.bookstore.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long customerId;
    private LocalDateTime orderDate;
    private List<OrderItem> items;
    private double totalAmount;

    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
    }

    public Order(Long id, Long customerId) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Helper methods
    public void addItem(OrderItem item) {
        items.add(item);
        calculateTotal();
    }

    private void calculateTotal() {
        this.totalAmount = 0;
        for (OrderItem item : items) {
            this.totalAmount += item.getSubtotal();
        }
    }
}
