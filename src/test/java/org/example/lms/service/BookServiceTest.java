package org.example.lms.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Book;
import org.example.lms.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);
    }

    @Test
    public void testAddBookSuccessfully() {
        Book book = new Book("123456789", "Called to Action", "James Mukasa", true, 1);

        Mockito.when(bookRepository.getBookFromDatabase(book.getIsbn())).thenReturn(null);

        bookService.addBook(book);

        verify(bookRepository, times(1)).addBookToDatabase(book);
    }

    @Test
    public void testAddBookThrowsExceptionWhenBookExists() {
        Book book = new Book("123456789", "Called to Action", "James Mukasa", true, 1);
        Book existingBook = new Book("123456789", "Not Called Book", "Jane Makeka", true, 1);

        Mockito.when(bookRepository.getBookFromDatabase(book.getIsbn())).thenReturn(existingBook);

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));

        verify(bookRepository, times(0)).addBookToDatabase(book);
    }

    @Test
    public void testUpdateBookSuccessfully() {
        Book book = new Book("123456789", "Called to Action", "James Mukasa", true, 1);

        Mockito.when(bookRepository.getBookFromDatabase(book.getIsbn())).thenReturn(book);

        bookService.updateBook(book);

        verify(bookRepository, times(1)).updateBookInDatabase(book);
    }

    @Test
    public void testUpdateBookThrowsExceptionWhenBookNotFound() {
        Book book = new Book("123456789", "Called to Action", "James Mukasa", true, 1);

        Mockito.when(bookRepository.getBookFromDatabase(book.getIsbn())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(book));

        verify(bookRepository, times(0)).updateBookInDatabase(book);
    }

    @Test
    public void testDeleteBookSuccessfully() {
        Book book = new Book("123456789", "Called to Action", "James Mukasa", true, 1);

        Mockito.when(bookRepository.getBookFromDatabase(book.getIsbn())).thenReturn(book);

        bookService.deleteBook(book.getIsbn());

        verify(bookRepository, times(1)).deleteBookFromDatabase(book.getIsbn());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " " }) // Testing with empty strings and whitespace
    public void testDeleteBookThrowsExceptionWhenIsbnIsInvalid(String invalidIsbn) {
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(invalidIsbn), "ISBN cannot be null or empty.");
    }

    @Test
    public void testDeleteBookThrowsExceptionWhenIsbnIsNull() {
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(null), "ISBN cannot be null or empty.");
    }

    @Test
    public void testDeleteBookThrowsExceptionWhenBookNotFound() {
        String isbn = "123456789";

        Mockito.when(bookRepository.getBookFromDatabase(isbn)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(isbn));

        verify(bookRepository, times(0)).deleteBookFromDatabase(isbn);
    }
    @Test
    public void testGetBookSuccessfully() {
        Book book = new Book("123456789", "Called to Action", "James Mukasa", true, 1);

        Mockito.when(bookRepository.getBookFromDatabase(book.getIsbn())).thenReturn(book);

        Book retrievedBook = bookService.getBook(book.getIsbn());

        assertEquals(book, retrievedBook);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " " }) // Include both empty string and whitespace
    public void testGetBookThrowsExceptionWhenIsbnIsInvalid(String invalidIsbn) {
        assertThrows(IllegalArgumentException.class, () -> bookService.getBook(invalidIsbn), "ISBN cannot be null or empty.");
    }

    @Test
    public void testGetBookThrowsExceptionWhenBookNotFound() {
        String isbn = "123456789";

        Mockito.when(bookRepository.getBookFromDatabase(isbn)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> bookService.getBook(isbn), "Book not found.");
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book("123456789", "Called to Action", "James Mukasa", true, 1);
        Book book2 = new Book("123456789", "Not Called Book", "Jane Makeka", true, 1);
        List<Book> books = Arrays.asList(book1, book2);

        Mockito.when(bookRepository.getAllBooks()).thenReturn(books);

        ObservableList<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
    }

    @Test
    public void testGetAvailableBooks() {
        Book book1 = new Book("123456789", "Called to Action", "James Mukasa", true, 1);
        Book book2 = new Book("123456789", "Not Called Book", "Jane Makeka", true, 1);
        List<Book> availableBooks = Arrays.asList(book1, book2);

        Mockito.when(bookRepository.findAvailableBooks()).thenReturn(availableBooks);

        ObservableList<Book> result = bookService.getAvailableBooks();

        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
    }

    @Test
    public void testSearchBooks() {
        Book book1 = new Book("123456789", "Called to Action", "James Mukasa", true, 1);
        List<Book> foundBooks = Arrays.asList(book1);

        Mockito.when(bookRepository.searchBooks("Mockito")).thenReturn(FXCollections.observableArrayList(foundBooks));

        ObservableList<Book> result = bookService.searchBooks("Mockito");

        assertEquals(1, result.size());
        assertTrue(result.contains(book1));
    }

    @Test
    public void testValidateBookSuccessfully() {
        Book book = new Book("123456789", "Called to Action", "James Mukasa", true, 1);

        assertDoesNotThrow(() -> bookService.validateBook(book));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBooks")
    public void testValidateBookThrowsException(Book book, String expectedMessage) {
        assertThrows(IllegalArgumentException.class, () -> bookService.validateBook(book), expectedMessage);
    }

    private static Stream<Arguments> provideInvalidBooks() {
        return Stream.of(
                Arguments.of(null, "Book cannot be null."),
                Arguments.of(new Book(null, "Title", "Author", true, 1), "ISBN cannot be null or empty."),
                Arguments.of(new Book("", "Title", "Author", true, 1), "ISBN cannot be null or empty."),
                Arguments.of(new Book("123456789", null, "Author", true, 1), "Title cannot be null or empty."),
                Arguments.of(new Book("123456789", "", "Author", true, 1), "Title cannot be null or empty."),
                Arguments.of(new Book("123456789", "Title", null, true, 1), "Author cannot be null or empty."),
                Arguments.of(new Book("123456789", "Title", "", true, 1), "Author cannot be null or empty.")
        );
    }

}
