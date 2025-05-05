package com.bookstore.repositories;

import com.bookstore.models.Cart;

import java.util.HashMap;
import java.util.Map;

public class CartRepository {
    private static CartRepository instance;
    private final Map<Long, Cart> carts; // customerId -> Cart

    private CartRepository() {
        this.carts = new HashMap<>();
    }

    public static synchronized CartRepository getInstance() {
        if (instance == null) {
            instance = new CartRepository();
        }
        return instance;
    }

    public Cart getCartByCustomerId(Long customerId) {
        return carts.get(customerId);
    }

    public void createCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    public void updateCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    public void deleteCart(Long customerId) {
        carts.remove(customerId);
    }

    public boolean exists(Long customerId) {
        return carts.containsKey(customerId);
    }
}
