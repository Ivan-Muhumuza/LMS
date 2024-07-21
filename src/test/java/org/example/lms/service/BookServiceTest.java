package org.example.lms.service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Book;
import org.example.lms.repository.BookRepository;
import org.example.lms.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testAddBook() {
        Book book = new Book("12345", "Title", "Author", true, 1);

        bookService.addBook(book);

        verify(bookRepository, times(1)).addBookToDatabase(book);
    }

    @Test
    void testUpdateBook() {
        Book book = new Book("12345", "Updated Title", "Updated Author", true, 1);
        Book existingBook = new Book("12345", "Old Title", "Old Author", true, 1);

        // Stub the getBookFromDatabase method
        when(bookRepository.getBookFromDatabase("12345")).thenReturn(existingBook);

        // Call the method under test
        bookService.updateBook(book);

        // Verify that the repository's updateBookInDatabase method was called with the correct book
        verify(bookRepository, times(1)).updateBookInDatabase(book);
    }

    @Test
    void testDeleteBook(){
        String isbn = "12345";
        Book book = new Book(isbn, "Title", "Author", true, 1);
        when(bookRepository.getBookFromDatabase(isbn)).thenReturn(book);

        bookService.deleteBook(isbn);

        verify(bookRepository).deleteBookFromDatabase(isbn);
    }

    @Test
    void testGetBook() {
        String isbn = "12345";
        Book book = new Book(isbn, "Title", "Author", true, 1);
        when(bookRepository.getBookFromDatabase(isbn)).thenReturn(book);

        Book result = bookService.getBook(isbn);

        assertEquals(book, result);
    }


    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("12345", "Title1", "Author1", true, 1));
        books.add(new Book("67890", "Title2", "Author2", true, 2));
        when(bookRepository.getAllBooks()).thenReturn(books);

        ObservableList<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    void testGetAvailableBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("12345", "Title1", "Author1", true, 1));
        when(bookRepository.findAvailableBooks()).thenReturn(books);

        ObservableList<Book> result = bookService.getAvailableBooks();

        assertEquals(1, result.size());
    }

    @Test
    void testSearchBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("12345", "Title1", "Author1", true, 1));
        when(bookRepository.searchBooks(anyString())).thenReturn(FXCollections.observableArrayList(books));

        ObservableList<Book> result = bookService.searchBooks("Title1");

        assertEquals(1, result.size());
    }
}
