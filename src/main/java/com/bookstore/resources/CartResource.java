package com.bookstore.resources;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.exceptions.CartNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.exceptions.OutOfStockException;
import com.bookstore.models.Book;
import com.bookstore.models.Cart;
import com.bookstore.models.CartItem;
import com.bookstore.repositories.BookRepository;
import com.bookstore.repositories.CartRepository;
import com.bookstore.repositories.CustomerRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final CartRepository cartRepository = CartRepository.getInstance();
    private final CustomerRepository customerRepository = CustomerRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") Long customerId, CartItem cartItem) {
        // Validate customer
        customerRepository.getCustomerById(customerId);
        
        // Validate book
        Book book = bookRepository.getBookById(cartItem.getBookId());
        
        // Validate quantity
        if (cartItem.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero");
        }
        
        // Check stock
        if (book.getStock() < cartItem.getQuantity()) {
            throw new OutOfStockException(book.getId(), cartItem.getQuantity(), book.getStock());
        }
        
        // Get or create cart
        Cart cart = cartRepository.getCartByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart(customerId);
            cartRepository.createCart(cart);
        }
        
        // Add item to cart
        cart.addItem(cartItem);
        cartRepository.updateCart(cart);
        
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") Long customerId) {
        // Validate customer
        customerRepository.getCustomerById(customerId);
        
        // Get cart
        Cart cart = cartRepository.getCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateCartItem(
            @PathParam("customerId") Long customerId,
            @PathParam("bookId") Long bookId,
            @QueryParam("quantity") int quantity) {
        
        // Validate customer
        customerRepository.getCustomerById(customerId);
        
        // Validate book
        Book book = bookRepository.getBookById(bookId);
        
        // Validate quantity
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero");
        }
        
        // Check stock
        if (book.getStock() < quantity) {
            throw new OutOfStockException(book.getId(), quantity, book.getStock());
        }
        
        // Get cart
        Cart cart = cartRepository.getCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        
        // Check if item exists in cart
        CartItem existingItem = cart.findItem(bookId);
        if (existingItem == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found in cart");
        }
        
        // Update item
        cart.updateItem(bookId, quantity);
        cartRepository.updateCart(cart);
        
        return cart;
    }

    @DELETE
    @Path("/items/{bookId}")
    public Cart removeCartItem(
            @PathParam("customerId") Long customerId,
            @PathParam("bookId") Long bookId) {
        
        // Validate customer
        customerRepository.getCustomerById(customerId);
        
        // Validate book exists
        bookRepository.getBookById(bookId);
        
        // Get cart
        Cart cart = cartRepository.getCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        
        // Check if item exists in cart
        CartItem existingItem = cart.findItem(bookId);
        if (existingItem == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found in cart");
        }
        
        // Remove item
        cart.removeItem(bookId);
        cartRepository.updateCart(cart);
        
        return cart;
    }
}
