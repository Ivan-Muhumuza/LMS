package org.example.lms.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.example.lms.model.Transaction;
import org.example.lms.util.DatabaseUtil;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionRepositoryIntegrationTest {

    private TransactionRepository transactionRepository;
    private static Connection connection;

    private final int testTransactionID = 1;
    private final Transaction testTransaction = new Transaction(testTransactionID, 1, "123456789",
            LocalDateTime.now(), LocalDateTime.now().plusDays(7), 1);

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
        transactionRepository = new TransactionRepository();

        // Initialize database schema before each test
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Transaction (" +
                    "TransactionID INT PRIMARY KEY," +
                    "PatronID INT," +
                    "BookIsbn VARCHAR(255)," +
                    "TransactionDate TIMESTAMP," +
                    "DueDate TIMESTAMP," +
                    "TypeID INT" +
                    ")");
        }

        // Insert test data
        transactionRepository.addTransactionToDatabase(testTransaction);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data after each test
        try (var stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Transaction WHERE TransactionID = " + testTransactionID);
        }
    }

    @Test
    public void testAddTransactionToDatabase() throws SQLException {
        Transaction retrievedTransaction = transactionRepository.getTransactionFromDatabase(testTransactionID);
        assertNotNull(retrievedTransaction);
        assertEquals(testTransaction.getPatronID(), retrievedTransaction.getPatronID());
        assertEquals(testTransaction.getBookIsbn(), retrievedTransaction.getBookIsbn());
    }

    @Test
    public void testUpdateTransactionInDatabase() throws SQLException {
        testTransaction.setPatronID(2);
        testTransaction.setBookIsbn("987654321");
        transactionRepository.updateTransactionInDatabase(testTransaction);

        Transaction retrievedTransaction = transactionRepository.getTransactionFromDatabase(testTransactionID);
        assertNotNull(retrievedTransaction);
        assertEquals(2, retrievedTransaction.getPatronID());
        assertEquals("987654321", retrievedTransaction.getBookIsbn());
    }

    @Test
    public void testDeleteTransactionFromDatabase() throws SQLException {
        transactionRepository.deleteTransactionFromDatabase(testTransactionID);

        Transaction retrievedTransaction = transactionRepository.getTransactionFromDatabase(testTransactionID);
        assertNull(retrievedTransaction);
    }

    @Test
    public void testGetTransactionFromDatabase() throws SQLException {
        // Retrieve the transaction from the database
        Transaction retrievedTransaction = transactionRepository.getTransactionFromDatabase(testTransactionID);

        // Verify that the transaction is correctly retrieved
        assertNotNull(retrievedTransaction);
        assertEquals(testTransactionID, retrievedTransaction.getTransactionID());
        assertEquals(testTransaction.getPatronID(), retrievedTransaction.getPatronID());
        assertEquals(testTransaction.getBookIsbn(), retrievedTransaction.getBookIsbn());
        assertEquals(testTransaction.getTransactionDate(), retrievedTransaction.getTransactionDate());
        assertEquals(testTransaction.getDueDate(), retrievedTransaction.getDueDate());
        assertEquals(testTransaction.getTypeID(), retrievedTransaction.getTypeID());
    }

    @Test
    public void testGetAllTransactions() throws SQLException {
        // Insert additional test data
        Transaction anotherTransaction = new Transaction(2, 3, "1122334455",
                LocalDateTime.now(), LocalDateTime.now().plusDays(7), 2);
        transactionRepository.addTransactionToDatabase(anotherTransaction);

        List<Transaction> transactions = transactionRepository.getAllTransactions();
        assertEquals(2, transactions.size());
    }
}

