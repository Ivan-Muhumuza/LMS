package org.example.lms.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.example.lms.model.Book;
import org.example.lms.model.BorrowedBook;
import org.example.lms.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public class BorrowedBookRepositoryIntegrationTest {
    private static Connection connection;
    private BorrowedBookRepository borrowedBookRepository;
    private BookRepository bookRepository;
    private final String testIsbn = "1234567890123";

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
        setupDatabaseSchema();

        // Initialize repositories
        borrowedBookRepository = new BorrowedBookRepository(connection);
        bookRepository = new BookRepository();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM BorrowedBooks");
            stmt.execute("DELETE FROM Book");
        }
    }

    private void setupDatabaseSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create Book table if not exists
            stmt.execute("CREATE TABLE IF NOT EXISTS Book (Isbn VARCHAR(13) PRIMARY KEY, Title VARCHAR(255), Author VARCHAR(255), IsAvailable BOOLEAN, LibraryID INT)");

            // Create BorrowedBooks table if not exists
            stmt.execute("CREATE TABLE IF NOT EXISTS BorrowedBooks (PatronID BIGINT, Isbn VARCHAR(13), BorrowedDate TIMESTAMP, DueDate TIMESTAMP, PRIMARY KEY (PatronID, Isbn))");
        }
    }

    @Test
    public void testAddBorrowedBook() throws SQLException {
        Book book = new Book(testIsbn, "Test Book", "Test Author", true, 1);
        bookRepository.addBookToDatabase(book);

        BorrowedBook borrowedBook = new BorrowedBook(1L, testIsbn, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        borrowedBookRepository.addBorrowedBook(borrowedBook);

        BorrowedBook retrievedBorrowedBook = borrowedBookRepository.getBorrowedBook(testIsbn);

        assertEquals(borrowedBook, retrievedBorrowedBook);
    }

    @Test
    public void testReturnBook() throws SQLException {
        Book book = new Book(testIsbn, "Test Book", "Test Author", false, 1);
        bookRepository.addBookToDatabase(book);

        BorrowedBook borrowedBook = new BorrowedBook(1L, testIsbn, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        borrowedBookRepository.addBorrowedBook(borrowedBook);

        borrowedBookRepository.returnBook(1L, testIsbn);

        List<BorrowedBook> borrowedBooks = borrowedBookRepository.getBorrowedBooksByPatron(1L);
        assertTrue(borrowedBooks.isEmpty());

        // Verify the book is available again
        Book updatedBook = bookRepository.getBookFromDatabase(testIsbn);
        assertTrue(updatedBook.isAvailable());
    }

    @Test
    public void testGetAllBorrowedBooks() throws SQLException {
        Book book1 = new Book(testIsbn, "Test Book 1", "Test Author 1", true, 1);
        Book book2 = new Book("9876543210987", "Test Book 2", "Test Author 2", true, 1);
        bookRepository.addBookToDatabase(book1);
        bookRepository.addBookToDatabase(book2);

        BorrowedBook borrowedBook1 = new BorrowedBook(1L, testIsbn, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        BorrowedBook borrowedBook2 = new BorrowedBook(2L, "9876543210987", LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        borrowedBookRepository.addBorrowedBook(borrowedBook1);
        borrowedBookRepository.addBorrowedBook(borrowedBook2);

        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findAllBorrowedBooks();
        assertEquals(2, borrowedBooks.size());
    }

    @Test
    public void testGetBorrowedBooksByPatron() throws SQLException {
        Book book = new Book(testIsbn, "Test Book", "Test Author", true, 1);
        bookRepository.addBookToDatabase(book);

        BorrowedBook borrowedBook = new BorrowedBook(1L, testIsbn, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        borrowedBookRepository.addBorrowedBook(borrowedBook);

        List<BorrowedBook> borrowedBooks = borrowedBookRepository.getBorrowedBooksByPatron(1L);
        assertEquals(1, borrowedBooks.size());
        assertEquals(borrowedBook, borrowedBooks.get(0));
    }

}