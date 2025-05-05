package com.bookstore.exceptions.mappers;

import com.bookstore.exceptions.OutOfStockException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException> {
    
    @Override
    public Response toResponse(OutOfStockException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Out of Stock");
        error.put("message", exception.getMessage());
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
