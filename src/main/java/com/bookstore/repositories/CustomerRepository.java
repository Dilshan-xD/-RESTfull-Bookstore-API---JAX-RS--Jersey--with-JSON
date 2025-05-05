package com.bookstore.repositories;

import com.bookstore.exceptions.CustomerNotFoundException;
import com.bookstore.models.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerRepository {
    private static CustomerRepository instance;
    private final Map<Long, Customer> customers;
    private final AtomicLong idCounter;

    private CustomerRepository() {
        this.customers = new HashMap<>();
        this.idCounter = new AtomicLong(1);
        // Initialize with some sample data
        addSampleData();
    }

    public static synchronized CustomerRepository getInstance() {
        if (instance == null) {
            instance = new CustomerRepository();
        }
        return instance;
    }

    private void addSampleData() {
        Customer customer1 = new Customer(idCounter.getAndIncrement(), "John Doe", "john.doe@example.com", "password123");
        Customer customer2 = new Customer(idCounter.getAndIncrement(), "Jane Smith", "jane.smith@example.com", "password456");
        
        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Customer getCustomerById(Long id) {
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }
        return customer;
    }

    public Customer createCustomer(Customer customer) {
        customer.setId(idCounter.getAndIncrement());
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        if (!customers.containsKey(id)) {
            throw new CustomerNotFoundException(id);
        }
        updatedCustomer.setId(id);
        customers.put(id, updatedCustomer);
        return updatedCustomer;
    }

    public void deleteCustomer(Long id) {
        if (!customers.containsKey(id)) {
            throw new CustomerNotFoundException(id);
        }
        customers.remove(id);
    }

    public boolean exists(Long id) {
        return customers.containsKey(id);
    }
}
