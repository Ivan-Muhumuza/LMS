package org.example.lms.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Book;
import org.example.lms.repository.BookRepository;

import java.util.List;

public class BookService {

    private final BookRepository bookRepository;

    public BookService() {
        this.bookRepository = new BookRepository();
    }

    public void addBook(Book book) throws IllegalArgumentException {
        validateBook(book);
        Book existingBook = bookRepository.getBookFromDatabase(book.getIsbn());
        if (existingBook != null) {
            throw new IllegalArgumentException("Book with this ISBN already exists.");
        }
        bookRepository.addBookToDatabase(book);
    }

    public void updateBook(Book book) throws IllegalArgumentException {
        validateBook(book);
        Book existingBook = bookRepository.getBookFromDatabase(book.getIsbn());
        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found.");
        }
        bookRepository.updateBookInDatabase(book);
    }

    public void deleteBook(String isbn) throws IllegalArgumentException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        Book existingBook = bookRepository.getBookFromDatabase(isbn);
        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found.");
        }
        bookRepository.deleteBookFromDatabase(isbn);
    }

    public Book getBook(String isbn) throws IllegalArgumentException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        Book book = bookRepository.getBookFromDatabase(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }
        return book;
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }

    }

    public ObservableList<Book> getAllBooks() {
        // Fetch all books
        return (ObservableList<Book>) bookRepository.getAllBooks();
    }

    public ObservableList<Book> getAvailableBooks() {
        List<Book> bookList = bookRepository.findAvailableBooks();
        return FXCollections.observableArrayList(bookList);
    }


    public ObservableList<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }


}

