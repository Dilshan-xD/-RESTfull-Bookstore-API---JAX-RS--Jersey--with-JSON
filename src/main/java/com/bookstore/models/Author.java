package com.bookstore.models;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private Long id;
    private String name;
    private String biography;
    private List<Long> bookIds;

    public Author() {
        this.bookIds = new ArrayList<>();
    }

    public Author(Long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.bookIds = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }

    public void addBookId(Long bookId) {
        if (!this.bookIds.contains(bookId)) {
            this.bookIds.add(bookId);
        }
    }

    public void removeBookId(Long bookId) {
        this.bookIds.remove(bookId);
    }
}

