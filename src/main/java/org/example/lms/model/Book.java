package org.example.lms.model;

import java.util.Objects;

public class Book {
    private String Isbn;
    private String Title;
    private String Author;
    private boolean IsAvailable;
    private Integer  LibraryID;

    public Book(String Isbn, String Title, String Author, boolean IsAvailable, Integer LibraryID) {
        this.Isbn = Isbn;
        this.Title = Title;
        this.Author = Author;
        this.IsAvailable = IsAvailable;
        this.LibraryID = LibraryID;
    }

    // Getters and setters
    public String getIsbn() { return Isbn; }
    public String getTitle() { return Title; }
    public void setTitle(String Title) { this.Title = Title; }
    public String getAuthor() { return Author; }
    public void setAuthor(String Author) { this.Author = Author; }
    public boolean isAvailable() { return IsAvailable; }
    public void setAvailable(boolean available) { IsAvailable = available; }
    public int getLibraryId() { return LibraryID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return IsAvailable == book.IsAvailable &&
                Objects.equals(Isbn, book.Isbn) &&
                Objects.equals(Title, book.Title) &&
                Objects.equals(Author, book.Author) &&
                Objects.equals(LibraryID, book.LibraryID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Isbn, Title, Author, IsAvailable, LibraryID);
    }

}