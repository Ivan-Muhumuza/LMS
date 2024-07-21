package org.example.lms.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.example.lms.model.BorrowedBook;
import org.example.lms.repository.BorrowedBookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class BorrowedBookRepositoryTest {

    private static Connection connection;
    private BorrowedBookRepository borrowedBookRepository;

    @BeforeAll
    static void setUpClass() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Book (Isbn VARCHAR(255) PRIMARY KEY, IsAvailable BOOLEAN)");
            stmt.execute("CREATE TABLE BorrowedBooks (PatronID BIGINT, Isbn VARCHAR(255), BorrowedDate TIMESTAMP, DueDate TIMESTAMP)");
            stmt.execute("INSERT INTO Book (Isbn, IsAvailable) VALUES ('12345', true)");
        }
    }

    @BeforeEach
    void setUp() {
        borrowedBookRepository = new BorrowedBookRepository(connection);
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        connection.close();
    }

    @Test
    void testAddBorrowedBook() throws SQLException {
        BorrowedBook borrowedBook = new BorrowedBook(1, "12345", LocalDateTime.now(), LocalDateTime.now().plusDays(14));

        borrowedBookRepository.addBorrowedBook(borrowedBook);

        var stmt = connection.createStatement();
        var rs = stmt.executeQuery("SELECT * FROM BorrowedBooks WHERE PatronID = 1 AND Isbn = '12345'");
        assertTrue(rs.next());
    }

    @Test
    void testReturnBook() throws SQLException {
        BorrowedBook borrowedBook = new BorrowedBook(1, "12345", LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        borrowedBookRepository.addBorrowedBook(borrowedBook);

        borrowedBookRepository.returnBook(1, "12345");

        var stmt = connection.createStatement();
        var rs = stmt.executeQuery("SELECT * FROM BorrowedBooks WHERE PatronID = 1 AND Isbn = '12345'");
        assertFalse(rs.next());

        rs = stmt.executeQuery("SELECT IsAvailable FROM Book WHERE Isbn = '12345'");
        assertTrue(rs.next());
        assertTrue(rs.getBoolean("IsAvailable"));
    }

    @Test
    void testGetBorrowedBooksByPatron() throws SQLException {
        BorrowedBook borrowedBook1 = new BorrowedBook(1, "12345", LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        BorrowedBook borrowedBook2 = new BorrowedBook(1, "67890", LocalDateTime.now(), LocalDateTime.now().plusDays(14));

        borrowedBookRepository.addBorrowedBook(borrowedBook1);
        borrowedBookRepository.addBorrowedBook(borrowedBook2);

        List<BorrowedBook> borrowedBooks = borrowedBookRepository.getBorrowedBooksByPatron(1);

        assertEquals(2, borrowedBooks.size());
    }

    @Test
    void testFindAllBorrowedBooks() throws SQLException {
        BorrowedBook borrowedBook1 = new BorrowedBook(1, "12345", LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        BorrowedBook borrowedBook2 = new BorrowedBook(2, "67890", LocalDateTime.now(), LocalDateTime.now().plusDays(14));

        borrowedBookRepository.addBorrowedBook(borrowedBook1);
        borrowedBookRepository.addBorrowedBook(borrowedBook2);

        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findAllBorrowedBooks();

        assertEquals(2, borrowedBooks.size());
    }
}
