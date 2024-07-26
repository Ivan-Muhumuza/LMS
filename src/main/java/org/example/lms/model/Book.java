package org.example.lms.model;

import java.util.Objects;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

public class Book {
    private final StringProperty isbn;
    private final StringProperty title;
    private final StringProperty author;
    private final BooleanProperty isAvailable;
    private final IntegerProperty libraryID;

    public Book(String isbn, String title, String author, boolean isAvailable, int libraryID) {
        this.isbn = new SimpleStringProperty(isbn);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isAvailable = new SimpleBooleanProperty(isAvailable);
        this.libraryID = new SimpleIntegerProperty(libraryID);
    }

    // Getters for JavaFX properties
    public StringProperty isbnProperty() {
        return isbn;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public BooleanProperty isAvailableProperty() {
        return isAvailable;
    }

    public IntegerProperty libraryIDProperty() {
        return libraryID;
    }

    // Getters for database operations
    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public boolean isAvailable() {
        return isAvailable.get();
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable.set(isAvailable);
    }

    public int getLibraryID() {
        return libraryID.get();
    }

    public void setLibraryID(int libraryID) {
        this.libraryID.set(libraryID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn.get(), book.isbn.get()) &&
                Objects.equals(title.get(), book.title.get()) &&
                Objects.equals(author.get(), book.author.get()) &&
                Objects.equals(isAvailable.get(), book.isAvailable.get()) &&
                Objects.equals(libraryID.get(), book.libraryID.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn.get(), title.get(), author.get(), isAvailable.get(), libraryID.get());
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn=" + isbn.get() +
                ", title=" + title.get() +
                ", author=" + author.get() +
                ", isAvailable=" + isAvailable.get() +
                ", libraryID=" + libraryID.get() +
                '}';
    }
}