package com.bookstore.resources;

import com.bookstore.exceptions.AuthorNotFoundException;
import com.bookstore.exceptions.InvalidInputException;
import com.bookstore.models.Book;
import com.bookstore.repositories.AuthorRepository;
import com.bookstore.repositories.BookRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final BookRepository bookRepository = BookRepository.getInstance();
    private final AuthorRepository authorRepository = AuthorRepository.getInstance();

    @POST
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        // Validate input
        validateBook(book);
        
        // Check if author exists
        if (!authorRepository.exists(book.getAuthorId())) {
            throw new AuthorNotFoundException(book.getAuthorId());
        }
        
        Book createdBook = bookRepository.createBook(book);
        
        // Add book to author's book list
        authorRepository.addBookToAuthor(book.getAuthorId(), createdBook.getId());
        
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdBook.getId())).build();
        return Response.created(uri).entity(createdBook).build();
    }

    @GET
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Book getBookById(@PathParam("id") Long id) {
        return bookRepository.getBookById(id);
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") Long id, Book book) {
        // Validate input
        validateBook(book);
        
        // Check if author exists
        if (!authorRepository.exists(book.getAuthorId())) {
            throw new AuthorNotFoundException(book.getAuthorId());
        }
        
        // Get the current book to check if author has changed
        Book currentBook = bookRepository.getBookById(id);
        
        // If author has changed, update the author's book lists
        if (!currentBook.getAuthorId().equals(book.getAuthorId())) {
            authorRepository.removeBookFromAuthor(currentBook.getAuthorId(), id);
            authorRepository.addBookToAuthor(book.getAuthorId(), id);
        }
        
        return bookRepository.updateBook(id, book);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        // Get the book to find its author
        Book book = bookRepository.getBookById(id);
        
        // Remove book from author's book list
        authorRepository.removeBookFromAuthor(book.getAuthorId(), id);
        
        // Delete the book
        bookRepository.deleteBook(id);
        
        return Response.noContent().build();
    }

    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN cannot be empty");
        }
        
        if (book.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than zero");
        }
        
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
    }
}
