package org.example.lms.repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.lms.model.Book;

public class BookRepositoryTest {

    private Connection connection;
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize H2 in-memory database
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        // Create table
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Book (" +
                    "Isbn VARCHAR(255) PRIMARY KEY, " +
                    "Title VARCHAR(255), " +
                    "Author VARCHAR(255), " +
                    "IsAvailable BOOLEAN, " +
                    "LibraryID INT)");
        }
        bookRepository = new BookRepository();
        // Set the connection to the repository
//        DatabaseUtil.setConnection(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // Clean up created books
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM Book");
            }

            connection.close();
        }
    }

    @Test
    void testAddBookToDatabase() {
        Book book = new Book("12345", "Test Title", "Test Author", true, 1);
        bookRepository.addBookToDatabase(book);

        Book retrievedBook = bookRepository.getBookFromDatabase("12345");
        assertNotNull(retrievedBook);
        assertEquals("12345", retrievedBook.getIsbn());
        assertEquals("Test Title", retrievedBook.getTitle());
        assertEquals("Test Author", retrievedBook.getAuthor());
        assertTrue(retrievedBook.isAvailable());
        assertEquals(1, retrievedBook.getLibraryID());
    }

    @Test
    void testUpdateBookInDatabase() {
        Book book = new Book("12345", "Old Title", "Old Author", true, 1);
        bookRepository.addBookToDatabase(book);

        book.setTitle("New Title");
        book.setAuthor("New Author");
        bookRepository.updateBookInDatabase(book);

        Book updatedBook = bookRepository.getBookFromDatabase("12345");
        assertNotNull(updatedBook);
        assertEquals("New Title", updatedBook.getTitle());
        assertEquals("New Author", updatedBook.getAuthor());
    }

    @Test
    void testDeleteBookFromDatabase() {
        Book book = new Book("12345", "Title", "Author", true, 1);
        bookRepository.addBookToDatabase(book);

        bookRepository.deleteBookFromDatabase("12345");

        Book deletedBook = bookRepository.getBookFromDatabase("12345");
        assertNull(deletedBook);
    }

    @Test
    void testFindAllBooks() {
        Book book1 = new Book("12345", "Title1", "Author1", true, 1);
        Book book2 = new Book("67890", "Title2", "Author2", false, 2);
        bookRepository.addBookToDatabase(book1);
        bookRepository.addBookToDatabase(book2);

        List<Book> allBooks = bookRepository.getAllBooks();
        assertEquals(2, allBooks.size());
    }

    @Test
    void testFindAvailableBooks() {
        Book book1 = new Book("12345", "Title1", "Author1", true, 1);
        Book book2 = new Book("67890", "Title2", "Author2", false, 2);
        bookRepository.addBookToDatabase(book1);
        bookRepository.addBookToDatabase(book2);

        List<Book> availableBooks = bookRepository.findAvailableBooks();
        assertEquals(1, availableBooks.size());
        assertEquals("12345", availableBooks.get(0).getIsbn());
    }

    @Test
    void testSearchBooks() {
        Book book1 = new Book("12345", "Java Programming", "Author1", true, 1);
        Book book2 = new Book("67890", "Python Programming", "Author2", true, 2);
        bookRepository.addBookToDatabase(book1);
        bookRepository.addBookToDatabase(book2);

        var searchResults = bookRepository.searchBooks("Programming");
        assertEquals(2, searchResults.size());
    }
}