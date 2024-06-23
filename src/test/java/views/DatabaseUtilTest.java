package views;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import java.sql.Statement;
import java.sql.Connection;

import java.time.LocalDateTime;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseUtilTest {

    private DatabaseUtil dbUtil;
    private Connection connection;
    private Patron patron;
    private BorrowedBook borrowedBook;

    private Transaction transaction;


    @BeforeAll
    void setUp() throws SQLException{
        dbUtil = new DatabaseUtil();

        // Ensure the Library exists
        String libraryIdQuery = "INSERT INTO library (LibraryID, Name) VALUES (1, 'Main Library') ON DUPLICATE KEY UPDATE Name=Name";
        dbUtil.getConnection().createStatement().executeUpdate(libraryIdQuery);

        // Ensure the Book exists
        String bookInsertQuery = "INSERT INTO book (ISBN, Title, Author, IsAvailable, LibraryID) " +
                "VALUES ('148-7-10-149780-6', 'Book Title 3', 'Author 3', true, 1) " +
                "ON DUPLICATE KEY UPDATE Title=Title";
        dbUtil.getConnection().createStatement().executeUpdate(bookInsertQuery);

        patron = new Patron(1, "John Doe", "john.doe@example.com");
        borrowedBook = new BorrowedBook(1, "978-3-16-148410-0", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().plusDays(14).truncatedTo(ChronoUnit.SECONDS));

        LocalDateTime transactionDate = LocalDateTime.of(2024, 6, 23, 10, 30, 15);
        LocalDateTime dueDate = LocalDateTime.of(2024, 6, 30, 10, 30, 15);

        transaction = new Transaction(1, 101, "978-3-16-148410-0", transactionDate, dueDate, 1);
    }

    @BeforeEach
    void addPatron() throws SQLException {
        if (dbUtil == null) {
            dbUtil = new DatabaseUtil();
        }

        String patronInsertQuery = "INSERT INTO Patron (PatronID, Name, Email) VALUES (1, 'John Doe', 'john.doe@example.com') " +
                "ON DUPLICATE KEY UPDATE Name=VALUES(Name), Email=VALUES(Email)";

        dbUtil.getConnection().createStatement().executeUpdate(patronInsertQuery);

        dbUtil.getConnection().createStatement().executeUpdate("INSERT INTO Patron (PatronID, Name, Email) VALUES (102, 'Patron102', 'patron102@example.com') ON DUPLICATE KEY UPDATE Name='Patron102', Email='patron102@example.com'");
        dbUtil.getConnection().createStatement().executeUpdate("INSERT INTO Patron (PatronID, Name, Email) VALUES (103, 'Patron103', 'patron103@example.com') ON DUPLICATE KEY UPDATE Name='Patron103', Email='patron103@example.com'");
        dbUtil.getConnection().createStatement().executeUpdate("INSERT INTO Book (Isbn, Title, Author) VALUES ('978-3-16-148410-0', 'Test Book 1', 'Author 1') ON DUPLICATE KEY UPDATE Title='Test Book 1', Author='Author 1'");
        dbUtil.getConnection().createStatement().executeUpdate("INSERT INTO Book (Isbn, Title, Author) VALUES ('978-3-16-666666-6', 'Test Book 2', 'Author 2') ON DUPLICATE KEY UPDATE Title='Test Book 2', Author='Author 2'");

    }

    @AfterEach
    void tearDown() throws SQLException {

        dbUtil.deleteBookFromDatabase("978-3-16-148415-8");

        dbUtil.deleteBorrowedBookFromDatabase(1, "978-3-16-148410-0");

        String patronDeleteQuery = "DELETE FROM Patron WHERE PatronID = 1";
        dbUtil.getConnection().createStatement().executeUpdate(patronDeleteQuery);

        String patron1DeleteQuery = "DELETE FROM Patron WHERE PatronID = 102";
        dbUtil.getConnection().createStatement().executeUpdate(patronDeleteQuery);

        String patron2DeleteQuery = "DELETE FROM Patron WHERE PatronID = 103";
        dbUtil.getConnection().createStatement().executeUpdate(patronDeleteQuery);

        String borrowedBookDeleteQuery = "DELETE FROM BorrowedBooks WHERE PatronID = 1 AND Isbn = '978-3-16-148410-0'";
        dbUtil.getConnection().createStatement().executeUpdate(borrowedBookDeleteQuery);

        String LibrarianDeleteQuery = "DELETE FROM Librarian WHERE LibrarianID = 1";
        dbUtil.getConnection().createStatement().executeUpdate(LibrarianDeleteQuery);

        dbUtil.getConnection().createStatement().executeUpdate("DELETE FROM Transaction");
        dbUtil.getConnection().createStatement().executeUpdate("DELETE FROM Patron");
        dbUtil.getConnection().createStatement().executeUpdate("DELETE FROM Book");

    }

//    @AfterEach
//    public void tearDown() throws SQLException {
//        dbUtil.deleteBookFromDatabase("978-3-16-148415-8");
//        dbUtil.deleteBorrowedBookFromDatabase(1, "978-3-16-148410-0");
//        dbUtil.close();
//    }

    private void createTestTable() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Transaction (TransactionID INT PRIMARY KEY, PatronID INT, BookIsbn VARCHAR(255), TransactionDate TIMESTAMP, DueDate TIMESTAMP, TypeID INT)";
        dbUtil.getConnection().createStatement().executeUpdate(createTableQuery);
    }

    private void dropTestTable() throws SQLException {
        String dropTableQuery = "DROP TABLE IF EXISTS Transaction";
        dbUtil.getConnection().createStatement().executeUpdate(dropTableQuery);
    }

    @Test
    @DisplayName("Add a new Book to database")
    void testAddBookToDatabase() throws SQLException {
        Book book1 = new Book("978-3-16-148410-0", "Book Title 1", "Author 1", true, 1);
        Book book2 = new Book("271-8-76-219517-4", "Book Title 2", "Author 2", true, 1);

        dbUtil.addBookToDatabase(book1);
        dbUtil.addBookToDatabase(book2);

        Book retrievedBook1 = dbUtil.getBookFromDatabase("978-3-16-148410-0");
        Book retrievedBook2 = dbUtil.getBookFromDatabase("271-8-76-219517-4");

        assertEquals(book1, retrievedBook1);
        assertEquals(book2, retrievedBook2);
    }

    @Test
    @DisplayName("Delete a Book from database")
    void testDeleteBookFromDatabase() throws SQLException {
        // Check if the Book exists
        Book book = dbUtil.getBookFromDatabase("148-7-10-149780-6");

        // If the book does not exist, create a new instance and add it to the database
        if (book == null) {
            book = new Book("148-7-10-149780-6", "Book Title 3", "Author 3", true, 1);
            dbUtil.addBookToDatabase(book);
        }

        dbUtil.deleteBookFromDatabase("148-7-10-149780-6");

        Book deletedBook = dbUtil.getBookFromDatabase("148-7-10-149780-6");
        assertNull(deletedBook);
    }

    @Test
    @DisplayName("Retrieve book from database")
    void testGetBookFromDatabase() throws SQLException {
        Book book = new Book("978-3-16-148415-8", "Book Title 1", "Author 1", true, 1);
        dbUtil.addBookToDatabase(book);

        Book retrievedBook = dbUtil.getBookFromDatabase("978-3-16-148415-8");
        assertEquals(book, retrievedBook);
    }

    @Test
    @DisplayName("Add a new Patron to database")
    public void testAddPatronToDatabase() throws SQLException {
        Patron patron = new Patron(1, "John Doe", "john.doe@example.com");
        dbUtil.addPatronToDatabase(patron);

        Patron retrievedPatron = dbUtil.getPatronFromDatabase(1);
        assertNotNull(retrievedPatron);
        assertEquals(patron.getPatronID(), retrievedPatron.getPatronID());
        assertEquals(patron.getName(), retrievedPatron.getName());
        assertEquals(patron.getEmail(), retrievedPatron.getEmail());
    }

    @Test
    @DisplayName("Update Patron details")
    public void testUpdatePatronInDatabase() throws SQLException {
        Patron patron = new Patron(1, "Jane Doe", "jane.doe@example.com");
        dbUtil.updatePatronInDatabase(patron);

        Patron retrievedPatron = dbUtil.getPatronFromDatabase(1);
        assertNotNull(retrievedPatron);
        assertEquals(patron.getPatronID(), retrievedPatron.getPatronID());
        assertEquals(patron.getName(), retrievedPatron.getName());
        assertEquals(patron.getEmail(), retrievedPatron.getEmail());
    }

    @Test
    @DisplayName("Delete a Patron")
    public void testDeletePatronFromDatabase() throws SQLException {
        dbUtil.deletePatronFromDatabase(1);

        Patron retrievedPatron = dbUtil.getPatronFromDatabase(1);
        assertNull(retrievedPatron);
    }

    @Test
    @DisplayName("Retrieve Patron details from db")
    public void testGetPatronFromDatabase() throws SQLException {
        Patron patron = new Patron(2, "Alice Smith", "alice.smith@example.com");
        dbUtil.addPatronToDatabase(patron);

        Patron retrievedPatron = dbUtil.getPatronFromDatabase(2);
        assertNotNull(retrievedPatron);
        assertEquals(patron.getPatronID(), retrievedPatron.getPatronID());
        assertEquals(patron.getName(), retrievedPatron.getName());
        assertEquals(patron.getEmail(), retrievedPatron.getEmail());

        dbUtil.deletePatronFromDatabase(2); // Clean up after the test
    }

    @Test
    @DisplayName("Add borrowed book to database")
    void testAddBorrowedBookToDatabase() throws SQLException {
        dbUtil.addBorrowedBookToDatabase(borrowedBook);

        BorrowedBook retrievedBook = dbUtil.getBorrowedBookFromDatabase(1, "978-3-16-148410-0");
        assertNotNull(retrievedBook);

        // Assert using truncatedTo(ChronoUnit.SECONDS) to ensure consistent precision with MySQL DATETIME
        assertEquals(borrowedBook.getPatronID(), retrievedBook.getPatronID());
        assertEquals(borrowedBook.getIsbn(), retrievedBook.getIsbn());
        assertEquals(borrowedBook.getBorrowedDate().truncatedTo(ChronoUnit.SECONDS), retrievedBook.getBorrowedDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(borrowedBook.getDueDate().truncatedTo(ChronoUnit.SECONDS), retrievedBook.getDueDate().truncatedTo(ChronoUnit.SECONDS));

        // Clean up: delete the borrowed book from the database
        dbUtil.deleteBorrowedBookFromDatabase(1, "978-3-16-148410-0");
    }

    @Test
    @DisplayName("Retrieve borrowed book from database")
    void testGetBorrowedBookFromDatabase() throws SQLException {
        dbUtil.addBorrowedBookToDatabase(borrowedBook);

        BorrowedBook retrievedBook = dbUtil.getBorrowedBookFromDatabase(1, "978-3-16-148410-0");
        assertNotNull(retrievedBook);
        assertEquals(borrowedBook.getPatronID(), retrievedBook.getPatronID());
        assertEquals(borrowedBook.getIsbn(), retrievedBook.getIsbn());
        assertEquals(borrowedBook.getBorrowedDate().truncatedTo(ChronoUnit.SECONDS), retrievedBook.getBorrowedDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(borrowedBook.getDueDate().truncatedTo(ChronoUnit.SECONDS), retrievedBook.getDueDate().truncatedTo(ChronoUnit.SECONDS));

        dbUtil.deleteBorrowedBookFromDatabase(1, "978-3-16-148410-0");
    }

    @Test
    @DisplayName("Update borrowed book in database")
    void testUpdateBorrowedBookInDatabase() throws SQLException {
        dbUtil.addBorrowedBookToDatabase(borrowedBook);

        borrowedBook.setDueDate(LocalDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS));
        dbUtil.updateBorrowedBookInDatabase(borrowedBook);

        BorrowedBook retrievedBook = dbUtil.getBorrowedBookFromDatabase(1, "978-3-16-148410-0");
        assertNotNull(retrievedBook);
        assertEquals(borrowedBook.getDueDate().truncatedTo(ChronoUnit.SECONDS), retrievedBook.getDueDate().truncatedTo(ChronoUnit.SECONDS));

        dbUtil.deleteBorrowedBookFromDatabase(1, "978-3-16-148410-0");
    }

    @Test
    @DisplayName("Delete borrowed book from database")
    void testDeleteBorrowedBookFromDatabase() throws SQLException {
        dbUtil.addBorrowedBookToDatabase(borrowedBook);

        dbUtil.deleteBorrowedBookFromDatabase(1, "978-3-16-148410-0");

        BorrowedBook retrievedBook = dbUtil.getBorrowedBookFromDatabase(1, "978-3-16-148410-0");
        assertNull(retrievedBook);
    }


    @Test
    @DisplayName("Test addTransactionToDatabase")
    void testAddTransactionToDatabase() {
        Transaction transaction = new Transaction(1, 101, "978-3-16-148410-0",
                LocalDateTime.of(2024, 6, 23, 10, 30, 15),
                LocalDateTime.of(2024, 6, 30, 10, 30, 15),
                1);

        assertDoesNotThrow(() -> dbUtil.addTransactionToDatabase(transaction));

        // Verify if transaction was added correctly
        Transaction retrievedTransaction = assertDoesNotThrow(() -> dbUtil.getTransactionFromDatabase(1));
        assertNotNull(retrievedTransaction);
        assertEquals(transaction.getTransactionID(), retrievedTransaction.getTransactionID());
        assertEquals(transaction.getPatronID(), retrievedTransaction.getPatronID());
        assertEquals(transaction.getBookIsbn(), retrievedTransaction.getBookIsbn());
        assertEquals(transaction.getTransactionDate(), retrievedTransaction.getTransactionDate());
        assertEquals(transaction.getDueDate(), retrievedTransaction.getDueDate());
        assertEquals(transaction.getTypeID(), retrievedTransaction.getTypeID());
    }

    @Test
    @DisplayName("Test updateTransactionInDatabase")
    void testUpdateTransactionInDatabase() {

        Transaction transaction = new Transaction(2, 102, "978-3-16-149410-0 ",
                LocalDateTime.of(2024, 6, 24, 10, 30, 15),
                LocalDateTime.of(2024, 7, 1, 10, 30, 15),
                2);

        assertDoesNotThrow(() -> dbUtil.addTransactionToDatabase(transaction));

        // Modify transaction details
        transaction.setPatronID(103);
        transaction.setBookIsbn("978-3-16-666666-6");
        transaction.setTransactionDate(LocalDateTime.of(2024, 6, 25, 10, 30, 15));
        transaction.setDueDate(LocalDateTime.of(2024, 7, 2, 10, 30, 15));
        transaction.setTypeID(3);

        assertDoesNotThrow(() -> dbUtil.updateTransactionInDatabase(transaction));

        // Verify if transaction was updated correctly
        Transaction updatedTransaction = assertDoesNotThrow(() -> dbUtil.getTransactionFromDatabase(2));
        assertNotNull(updatedTransaction);
        assertEquals(transaction.getTransactionID(), updatedTransaction.getTransactionID());
        assertEquals(transaction.getPatronID(), updatedTransaction.getPatronID());
        assertEquals(transaction.getBookIsbn(), updatedTransaction.getBookIsbn());
        assertEquals(transaction.getTransactionDate(), updatedTransaction.getTransactionDate());
        assertEquals(transaction.getDueDate(), updatedTransaction.getDueDate());
        assertEquals(transaction.getTypeID(), updatedTransaction.getTypeID());
    }


    @Test
    @DisplayName("Test deleteTransactionFromDatabase")
    void testDeleteTransactionFromDatabase() {
        Transaction transaction = new Transaction(3, 103, "978-3-16-148440-0",
                LocalDateTime.of(2024, 6, 26, 10, 30, 15),
                LocalDateTime.of(2024, 7, 3, 10, 30, 15),
                4);

        assertDoesNotThrow(() -> dbUtil.addTransactionToDatabase(transaction));

        // Delete transaction
        assertDoesNotThrow(() -> dbUtil.deleteTransactionFromDatabase(3));

        // Verify if transaction was deleted
        Transaction deletedTransaction = assertDoesNotThrow(() -> dbUtil.getTransactionFromDatabase(3));
        assertNull(deletedTransaction);
    }

    @Test
    @DisplayName("Test getTransactionFromDatabase")
    void testGetTransactionFromDatabase() {
        Transaction transaction = new Transaction(4, 104, "978-3-16-148450-0",
                LocalDateTime.of(2024, 6, 27, 10, 30, 15),
                LocalDateTime.of(2024, 7, 4, 10, 30, 15),
                5);

        assertDoesNotThrow(() -> dbUtil.addTransactionToDatabase(transaction));

        // Retrieve transaction
        Transaction retrievedTransaction = assertDoesNotThrow(() -> dbUtil.getTransactionFromDatabase(4));
        assertNotNull(retrievedTransaction);
        assertEquals(transaction.getTransactionID(), retrievedTransaction.getTransactionID());
        assertEquals(transaction.getPatronID(), retrievedTransaction.getPatronID());
        assertEquals(transaction.getBookIsbn(), retrievedTransaction.getBookIsbn());
        assertEquals(transaction.getTransactionDate(), retrievedTransaction.getTransactionDate());
        assertEquals(transaction.getDueDate(), retrievedTransaction.getDueDate());
        assertEquals(transaction.getTypeID(), retrievedTransaction.getTypeID());
    }

    @Test
    @DisplayName("Test addLibrarianToDatabase")
    void testAddLibrarianToDatabase() {
        Librarian librarian = new Librarian(1, 1, "John Doe", "john.doe@example.com");

        assertDoesNotThrow(() -> dbUtil.addLibrarianToDatabase(librarian));

        Librarian retrievedLibrarian = assertDoesNotThrow(() -> dbUtil.getLibrarianFromDatabase(1));
        assertNotNull(retrievedLibrarian);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
        assertEquals(librarian.getLibraryID(), retrievedLibrarian.getLibraryID());
        assertEquals(librarian.getName(), retrievedLibrarian.getName());
        assertEquals(librarian.getEmail(), retrievedLibrarian.getEmail());
    }

    @Test
    @DisplayName("Test updateLibrarianInDatabase")
    void testUpdateLibrarianInDatabase() {
        Librarian librarian = new Librarian(1, 1, "John Doe", "john.doe@example.com");

        assertDoesNotThrow(() -> dbUtil.addLibrarianToDatabase(librarian));

        librarian.setName("Updated Name");
        librarian.setEmail("updated.email@example.com");

        assertDoesNotThrow(() -> dbUtil.updateLibrarianInDatabase(librarian));

        Librarian retrievedLibrarian = assertDoesNotThrow(() -> dbUtil.getLibrarianFromDatabase(1));
        assertNotNull(retrievedLibrarian);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
        assertEquals(librarian.getLibraryID(), retrievedLibrarian.getLibraryID());
        assertEquals(librarian.getName(), retrievedLibrarian.getName());
        assertEquals(librarian.getEmail(), retrievedLibrarian.getEmail());
    }

    @Test
    @DisplayName("Test deleteLibrarianFromDatabase")
    void testDeleteLibrarianFromDatabase() {
        Librarian librarian = new Librarian(1, 1, "John Doe", "john.doe@example.com");

        assertDoesNotThrow(() -> dbUtil.addLibrarianToDatabase(librarian));

        assertDoesNotThrow(() -> dbUtil.deleteLibrarianFromDatabase(1));

        Librarian retrievedLibrarian = assertDoesNotThrow(() -> dbUtil.getLibrarianFromDatabase(1));
        assertNull(retrievedLibrarian);
    }

    @Test
    @DisplayName("Test getLibrarianFromDatabase")
    void testGetLibrarianFromDatabase() {
        Librarian librarian = new Librarian(1, 1, "John Doe", "john.doe@example.com");

        assertDoesNotThrow(() -> dbUtil.addLibrarianToDatabase(librarian));

        Librarian retrievedLibrarian = assertDoesNotThrow(() -> dbUtil.getLibrarianFromDatabase(1));
        assertNotNull(retrievedLibrarian);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
        assertEquals(librarian.getLibraryID(), retrievedLibrarian.getLibraryID());
        assertEquals(librarian.getName(), retrievedLibrarian.getName());
        assertEquals(librarian.getEmail(), retrievedLibrarian.getEmail());
    }



    @AfterAll
    void close() throws SQLException {
        // Close database connection after all tests are finished
        if (dbUtil != null) {
            dbUtil.close();
        }
    }
}