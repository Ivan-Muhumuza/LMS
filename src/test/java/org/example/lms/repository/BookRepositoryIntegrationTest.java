package org.example.lms.repository;

import org.example.lms.model.Book;
import org.example.lms.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryIntegrationTest {

    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddBookToDatabase() throws SQLException {
        Book book = new Book("12345", "Title", "Author", true, 1);
        bookRepository.addBookToDatabase(book);

        Book retrievedBook = bookRepository.getBookFromDatabase("12345");
        assertNotNull(retrievedBook);
        assertEquals("12345", retrievedBook.getIsbn());
        assertEquals("Title", retrievedBook.getTitle());
    }

    @Test
    void testUpdateBookInDatabase() throws SQLException {
        Book book = new Book("12345", "Title", "Author", true, 1);
        bookRepository.addBookToDatabase(book);

        book.setTitle("New Title");
        bookRepository.updateBookInDatabase(book);

        Book updatedBook = bookRepository.getBookFromDatabase("12345");
        assertEquals("New Title", updatedBook.getTitle());
    }

    @Test
    void testDeleteBookFromDatabase() throws SQLException {
        Book book = new Book("12345", "Title", "Author", true, 1);
        bookRepository.addBookToDatabase(book);
        bookRepository.deleteBookFromDatabase("12345");

        Book deletedBook = bookRepository.getBookFromDatabase("12345");
        assertNull(deletedBook);
    }

    @Test
    void testGetAllBooks() throws SQLException {
        Book book1 = new Book("12345", "Title1", "Author1", true, 1);
        Book book2 = new Book("67890", "Title2", "Author2", true, 1);
        bookRepository.addBookToDatabase(book1);
        bookRepository.addBookToDatabase(book2);

        List<Book> books = bookRepository.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void testFindAvailableBooks() throws SQLException {
        Book book1 = new Book("12345", "Title1", "Author1", true, 1);
        Book book2 = new Book("67890", "Title2", "Author2", false, 2);
        bookRepository.addBookToDatabase(book1);
        bookRepository.addBookToDatabase(book2);

        List<Book> availableBooks = bookRepository.findAvailableBooks();
        assertEquals(1, availableBooks.size());
        assertEquals("12345", availableBooks.get(0).getIsbn());
    }
}
