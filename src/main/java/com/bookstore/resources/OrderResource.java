package com.bookstore.resources;

import com.bookstore.exceptions.CartNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.exceptions.OutOfStockException;
import com.bookstore.models.*;
import com.bookstore.repositories.BookRepository;
import com.bookstore.repositories.CartRepository;
import com.bookstore.repositories.CustomerRepository;
import com.bookstore.repositories.OrderRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final OrderRepository orderRepository = OrderRepository.getInstance();
    private final CustomerRepository customerRepository = CustomerRepository.getInstance();
    private final CartRepository cartRepository = CartRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();

    @POST
    public Response createOrder(@PathParam("customerId") Long customerId, @Context UriInfo uriInfo) {
        // Validate customer
        customerRepository.getCustomerById(customerId);
        
        // Get cart
        Cart cart = cartRepository.getCartByCustomerId(customerId);
        if (cart == null || cart.isEmpty()) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " is empty or does not exist");
        }
        
        // Create order
        Order order = new Order();
        order.setCustomerId(customerId);
        
        // Convert cart items to order items and check stock
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            Book book = bookRepository.getBookById(cartItem.getBookId());
            
            // Check stock
            if (book.getStock() < cartItem.getQuantity()) {
                throw new OutOfStockException(book.getId(), cartItem.getQuantity(), book.getStock());
            }
            
            // Create order item
            OrderItem orderItem = new OrderItem(
                book.getId(),
                book.getTitle(),
                cartItem.getQuantity(),
                book.getPrice()
            );
            orderItems.add(orderItem);
            
            // Update book stock
            bookRepository.updateStock(book.getId(), cartItem.getQuantity());
        }
        
        order.setItems(orderItems);
        Order createdOrder = orderRepository.createOrder(order);
        
        // Clear the cart
        cart.clear();
        cartRepository.updateCart(cart);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdOrder.getId())).build();
        return Response.created(uri).entity(createdOrder).build();
    }

    @GET
    public List<Order> getCustomerOrders(@PathParam("customerId") Long customerId) {
        // Validate customer
        customerRepository.getCustomerById(customerId);
        
        return orderRepository.getOrdersByCustomerId(customerId);
    }

    @GET
    @Path("/{orderId}")
    public Order getOrder(@PathParam("customerId") Long customerId, @PathParam("orderId") Long orderId) {
        // Validate customer
        customerRepository.getCustomerById(customerId);
        
        return orderRepository.getOrderByCustomerIdAndOrderId(customerId, orderId);
    }
}
    