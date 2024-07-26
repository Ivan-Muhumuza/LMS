package org.example.lms.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Book;
import org.example.lms.service.BookService;
import org.example.lms.util.DatabaseUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryIntegrationTest {

    private BookRepository bookRepository;
    private static Connection connection;

    private final String testIsbn = "123456789";
    private final Book testBook = new Book(testIsbn, "Test Book", "Test Author", true, 1);

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        // Configure DatabaseUtil for test use (H2)
        DatabaseUtil.setUseTestDatabase(true);
        DatabaseUtil databaseUtil = DatabaseUtil.getInstance();
        connection = databaseUtil.getConnection();
    }

    @AfterAll
    public static void tearDownAfterClass() throws SQLException {
        // Clean up resources
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        bookRepository = new BookRepository();

        // Initialize database schema before each test
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Book (" +
                    "Isbn VARCHAR(255) PRIMARY KEY," +
                    "Title VARCHAR(255)," +
                    "Author VARCHAR(255)," +
                    "IsAvailable BOOLEAN," +
                    "LibraryID INT" +
                    ")");
        }

        // Insert test data
        bookRepository.addBookToDatabase(testBook);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data after each test
        try (var stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Book WHERE Isbn = '" + testIsbn + "'");
        }
    }

    @Test
    public void testAddBookToDatabase() throws SQLException {
        Book retrievedBook = bookRepository.getBookFromDatabase(testIsbn);
        assertNotNull(retrievedBook);
        assertEquals(testBook.getTitle(), retrievedBook.getTitle());
    }

    @Test
    public void testUpdateBookInDatabase() throws SQLException {
        testBook.setTitle("Updated Title");
        bookRepository.updateBookInDatabase(testBook);

        Book retrievedBook = bookRepository.getBookFromDatabase(testIsbn);
        assertEquals("Updated Title", retrievedBook.getTitle());
    }

    @Test
    public void testDeleteBookFromDatabase() throws SQLException {
        bookRepository.deleteBookFromDatabase(testIsbn);

        Book retrievedBook = bookRepository.getBookFromDatabase(testIsbn);
        assertNull(retrievedBook);
    }

    @Test
    public void testGetAllBooks() throws SQLException {
        Book book1 = new Book("987654321", "Test Book 2", "Test Author 2", true, 2);
        bookRepository.addBookToDatabase(book1);

        List<Book> books = bookRepository.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    public void testFindAvailableBooks() throws SQLException {
        clearBook();
        Book book2 = new Book("987654321", "Test Book 2", "Test Author 2", false, 2);
        bookRepository.addBookToDatabase(book2);

        List<Book> books = bookRepository.findAvailableBooks();
        assertEquals(1, books.size());
        assertTrue(books.get(0).isAvailable());
    }

    public void clearBook() throws SQLException {
        String sql = "DELETE FROM Book WHERE Isbn = '" + "987654321" + "'";
        try (var stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Test
    public void testSearchBooks() {
        BookRepository bookRepository = new BookRepository();

        // Act
        ObservableList<Book> result = bookRepository.searchBooks("Test");

        // Assert
        ObservableList<Book> expectedBooks = FXCollections.observableArrayList(
                new Book(testIsbn, "Test Book", "Test Author", true, 1)
        );

        assertEquals(expectedBooks, result, "The search result should match the expected books.");
    }
}