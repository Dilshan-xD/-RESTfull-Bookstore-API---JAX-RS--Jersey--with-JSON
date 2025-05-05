package com.bookstore.resources;

import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.models.Author;
import com.bookstore.models.Book;
import com.bookstore.repositories.AuthorRepository;
import com.bookstore.repositories.BookRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private final AuthorRepository authorRepository = AuthorRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();

    @POST
    public Response createAuthor(Author author, @Context UriInfo uriInfo) {
        // Validate input
        validateAuthor(author);
        
        Author createdAuthor = authorRepository.createAuthor(author);
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdAuthor.getId())).build();
        return Response.created(uri).entity(createdAuthor).build();
    }

    @GET
    public List<Author> getAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    @GET
    @Path("/{id}")
    public Author getAuthorById(@PathParam("id") Long id) {
        return authorRepository.getAuthorById(id);
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") Long id, Author author) {
        // Validate input
        validateAuthor(author);
        
        return authorRepository.updateAuthor(id, author);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        // Check if author has books
        List<Book> authorBooks = bookRepository.getBooksByAuthorId(id);
        if (!authorBooks.isEmpty()) {
            throw new InvalidInputException("Cannot delete author with existing books. Delete the books first.");
        }
        
        authorRepository.deleteAuthor(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getAuthorBooks(@PathParam("id") Long id) {
        // Verify author exists
        authorRepository.getAuthorById(id);
        
        // Get books by author
        return bookRepository.getBooksByAuthorId(id);
    }

    private void validateAuthor(Author author) {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name cannot be empty");
        }
    }
}
