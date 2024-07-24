package org.example.lms.repository;
import org.example.lms.model.Librarian;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.lms.util.DatabaseUtil;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarianRepositoryIntegrationTest {
    private static Connection connection;
    private LibrarianRepository librarianRepository;
    private final String testEmail = "test@library.com";
    private final int testLibrarianID = 1;

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
        librarianRepository = new LibrarianRepository();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data after each test
        try (var stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Librarian WHERE Email = '" + testEmail + "'");
        }
    }

    private void setupDatabaseSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create Librarian table
            stmt.execute("CREATE TABLE IF NOT EXISTS Librarian (" +
                    "LibrarianID INT PRIMARY KEY, " +
                    "LibraryID INT, " +
                    "Name VARCHAR(255), " +
                    "Email VARCHAR(255), " +
                    "Password VARCHAR(255))");
        }
    }

    @Test
    public void testAddLibrarian() throws SQLException {
        Librarian librarian = new Librarian(testLibrarianID, 1, "Test Librarian", testEmail, "password");
        librarianRepository.addLibrarianToDatabase(librarian);

        Librarian retrievedLibrarian = librarianRepository.getLibrarianByEmail(testEmail);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
        assertEquals(librarian.getLibraryID(), retrievedLibrarian.getLibraryID());
        assertEquals(librarian.getName(), retrievedLibrarian.getName());
        assertEquals(librarian.getEmail(), retrievedLibrarian.getEmail());
    }

    @Test
    public void testUpdateLibrarian() throws SQLException {
        Librarian librarian = new Librarian(testLibrarianID, 1, "Test Librarian", testEmail, "password");
        librarianRepository.addLibrarianToDatabase(librarian);

        Librarian updatedLibrarian = new Librarian(testLibrarianID, 2, "Updated Librarian", testEmail, "newpassword");
        librarianRepository.updateLibrarianInDatabase(updatedLibrarian);

        Librarian retrievedLibrarian = librarianRepository.getLibrarianByEmail(testEmail);
        assertEquals(updatedLibrarian.getLibraryID(), retrievedLibrarian.getLibraryID());
        assertEquals(updatedLibrarian.getName(), retrievedLibrarian.getName());
    }

    @Test
    public void testDeleteLibrarian() throws SQLException {
        Librarian librarian = new Librarian(testLibrarianID, 1, "Test Librarian", testEmail, "password");
        librarianRepository.addLibrarianToDatabase(librarian);

        librarianRepository.deleteLibrarianFromDatabase(testLibrarianID);

        Librarian retrievedLibrarian = librarianRepository.getLibrarianByEmail(testEmail);
        assertNull(retrievedLibrarian);
    }

    @Test
    public void testGetLibrarianByEmail() throws SQLException {
        Librarian librarian = new Librarian(testLibrarianID, 1, "Test Librarian", testEmail, "password");
        librarianRepository.addLibrarianToDatabase(librarian);

        Librarian retrievedLibrarian = librarianRepository.getLibrarianByEmail(testEmail);
        assertNotNull(retrievedLibrarian);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
    }

    @Test
    public void testGetLibrarianById() throws SQLException {
        Librarian librarian = new Librarian(testLibrarianID, 1, "Test Librarian", testEmail, "password");
        librarianRepository.addLibrarianToDatabase(librarian);

        Librarian retrievedLibrarian = librarianRepository.getLibrarianFromDatabase(testLibrarianID);
        assertNotNull(retrievedLibrarian);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
    }
}
