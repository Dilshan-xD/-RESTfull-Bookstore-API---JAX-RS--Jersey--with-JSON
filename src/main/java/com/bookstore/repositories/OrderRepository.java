package com.bookstore.repositories;

import com.bookstore.exceptions.OrderNotFoundException;
import com.bookstore.models.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class OrderRepository {
    private static OrderRepository instance;
    private final Map<Long, Order> orders;
    private final AtomicLong idCounter;

    private OrderRepository() {
        this.orders = new HashMap<>();
        this.idCounter = new AtomicLong(1);
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public Order createOrder(Order order) {
        order.setId(idCounter.getAndIncrement());
        orders.put(order.getId(), order);
        return order;
    }

    public Order getOrderById(Long id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new OrderNotFoundException("Order with ID " + id + " does not exist.");
        }
        return order;
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public Order getOrderByCustomerIdAndOrderId(Long customerId, Long orderId) {
        Order order = getOrderById(orderId);
        if (!order.getCustomerId().equals(customerId)) {
            throw new OrderNotFoundException("Order with ID " + orderId + " does not belong to customer with ID " + customerId);
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
}
