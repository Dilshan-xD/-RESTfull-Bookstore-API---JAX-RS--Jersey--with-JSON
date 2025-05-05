package com.bookstore.application;

import com.bookstore.exceptions.mappers.*;
import com.bookstore.resources.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class BookstoreApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Resources
        classes.add(BookResource.class);
        classes.add(AuthorResource.class);
        classes.add(CustomerResource.class);
        classes.add(CartResource.class);
        classes.add(OrderResource.class);
        
        // Exception mappers
        classes.add(BookNotFoundExceptionMapper.class);
        classes.add(AuthorNotFoundExceptionMapper.class);
        classes.add(CustomerNotFoundExceptionMapper.class);
        classes.add(CartNotFoundExceptionMapper.class);
        classes.add(InvalidInputExceptionMapper.class);
        classes.add(OutOfStockExceptionMapper.class);
        classes.add(GenericExceptionMapper.class);
        // In BookstoreApplication.java, add this to the getClasses() method:
        classes.add(OrderNotFoundExceptionMapper.class);

        return classes;
    }
}
