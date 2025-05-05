package com.bookstore.exceptions.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    
    @Override
    public Response toResponse(Throwable exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", exception.getMessage());
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
