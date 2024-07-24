package org.example.lms.repository;

import org.example.lms.model.Patron;
import org.example.lms.util.DatabaseUtil;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PatronRepositoryIntegrationTest {
    private static Connection connection;
    private PatronRepository patronRepository;
    private final long testPatronID = 1L;
    private final String testEmail = "test@example.com";

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

        // Initialize repository
        patronRepository = new PatronRepository();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data after each test
        try (var stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Patron");
        }
    }

    private void setupDatabaseSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create Patron table
            stmt.execute("CREATE TABLE IF NOT EXISTS Patron (PatronID BIGINT PRIMARY KEY, Name VARCHAR(255), Email VARCHAR(255))");
        }
    }

    @Test
    public void testAddPatron() throws SQLException {
        Patron patron = new Patron(testPatronID, "Test Patron", testEmail);
        patronRepository.addPatron(patron);

        Patron retrievedPatron = patronRepository.getPatron(testPatronID);
        assertNotNull(retrievedPatron);
        assertEquals(patron.getPatronID(), retrievedPatron.getPatronID());
        assertEquals(patron.getName(), retrievedPatron.getName());
        assertEquals(patron.getEmail(), retrievedPatron.getEmail());
    }

    @Test
    public void testUpdatePatron() throws SQLException {
        Patron patron = new Patron(testPatronID, "Test Patron", testEmail);
        patronRepository.addPatron(patron);

        Patron updatedPatron = new Patron(testPatronID, "Updated Name", "updated@example.com");
        patronRepository.updatePatron(updatedPatron);

        Patron retrievedPatron = patronRepository.getPatron(testPatronID);
        assertNotNull(retrievedPatron);
        assertEquals(updatedPatron.getName(), retrievedPatron.getName());
        assertEquals(updatedPatron.getEmail(), retrievedPatron.getEmail());
    }

    @Test
    public void testDeletePatron() throws SQLException {
        Patron patron = new Patron(testPatronID, "Test Patron", testEmail);
        patronRepository.addPatron(patron);

        patronRepository.deletePatron(testPatronID);
        Patron retrievedPatron = patronRepository.getPatron(testPatronID);
        assertNull(retrievedPatron);
    }

    @Test
    public void testGetAllPatrons() throws SQLException {
        Patron patron1 = new Patron(testPatronID, "Test Patron 1", "test1@example.com");
        Patron patron2 = new Patron(2L, "Test Patron 2", "test2@example.com");
        patronRepository.addPatron(patron1);
        patronRepository.addPatron(patron2);

        List<Patron> patrons = patronRepository.getAllPatrons();
        assertEquals(2, patrons.size());
        assertTrue(patrons.contains(patron1));
        assertTrue(patrons.contains(patron2));
    }

    @Test
    public void testGetPatronByEmail() throws SQLException {
        Patron patron = new Patron(testPatronID, "Test Patron", testEmail);
        patronRepository.addPatron(patron);

        Patron retrievedPatron = patronRepository.getPatronByEmail(testEmail);
        assertNotNull(retrievedPatron);
        assertEquals(patron.getPatronID(), retrievedPatron.getPatronID());
        assertEquals(patron.getName(), retrievedPatron.getName());
    }
}
