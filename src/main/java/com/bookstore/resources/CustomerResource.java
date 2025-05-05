package com.bookstore.resources;

import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.models.Customer;
import com.bookstore.repositories.CustomerRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private final CustomerRepository customerRepository = CustomerRepository.getInstance();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @POST
    public Response createCustomer(Customer customer, @Context UriInfo uriInfo) {
        // Validate input
        validateCustomer(customer);
        
        Customer createdCustomer = customerRepository.createCustomer(customer);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdCustomer.getId())).build();
        return Response.created(uri).entity(createdCustomer).build();
    }

    @GET
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomerById(@PathParam("id") Long id) {
        return customerRepository.getCustomerById(id);
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") Long id, Customer customer) {
        // Validate input
        validateCustomer(customer);
        
        return customerRepository.updateCustomer(id, customer);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        customerRepository.deleteCustomer(id);
        return Response.noContent().build();
    }

    private void validateCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty");
        }
        
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }
        
        if (!EMAIL_PATTERN.matcher(customer.getEmail()).matches()) {
            throw new InvalidInputException("Invalid email format");
        }
        
        if (customer.getPassword() == null || customer.getPassword().length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters long");
        }
    }
}
