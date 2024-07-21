package org.example.lms.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.h2.jdbcx.JdbcDataSource;
import org.example.lms.model.Patron;
import org.example.lms.repository.PatronRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatronRepositoryTest {

    private Connection connection;
    private PatronRepository patronRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up H2 in-memory database
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"); // In-memory database URL
        dataSource.setUser("sa");
        dataSource.setPassword("");

        connection = dataSource.getConnection();
        patronRepository = new PatronRepository();

        // Initialize the database schema
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Patron (" +
                    "PatronID BIGINT PRIMARY KEY, " +
                    "Name VARCHAR(255), " +
                    "Email VARCHAR(255))");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up the database
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Patron");
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testAddPatron() throws SQLException {
        Patron patron = new Patron(1, "Alice Johnson", "alice.johnson@example.com");
        patronRepository.addPatron(patron);

        Patron retrievedPatron = patronRepository.getPatron(1);
        assertNotNull(retrievedPatron);
        assertEquals(patron.getPatronID(), retrievedPatron.getPatronID());
        assertEquals(patron.getName(), retrievedPatron.getName());
        assertEquals(patron.getEmail(), retrievedPatron.getEmail());
    }

    @Test
    public void testUpdatePatron() throws SQLException {
        Patron patron = new Patron(1, "Alice Johnson", "alice.johnson@example.com");
        patronRepository.addPatron(patron);

        patron.setName("Alice Smith");
        patron.setEmail("alice.smith@example.com");
        patronRepository.updatePatron(patron);

        Patron updatedPatron = patronRepository.getPatron(1);
        assertNotNull(updatedPatron);
        assertEquals("Alice Smith", updatedPatron.getName());
        assertEquals("alice.smith@example.com", updatedPatron.getEmail());
    }

    @Test
    public void testDeletePatron() throws SQLException {
        Patron patron = new Patron(1, "Alice Johnson", "alice.johnson@example.com");
        patronRepository.addPatron(patron);

        patronRepository.deletePatron(1);
        Patron deletedPatron = patronRepository.getPatron(1);
        assertNull(deletedPatron);
    }

    @Test
    public void testGetAllPatrons() throws SQLException {
        Patron patron1 = new Patron(1, "Alice Johnson", "alice.johnson@example.com");
        Patron patron2 = new Patron(2, "Bob Brown", "bob.brown@example.com");
        patronRepository.addPatron(patron1);
        patronRepository.addPatron(patron2);

        List<Patron> patrons = patronRepository.getAllPatrons();
        assertEquals(2, patrons.size());
        assertTrue(patrons.stream().anyMatch(p -> p.getPatronID() == 1));
        assertTrue(patrons.stream().anyMatch(p -> p.getPatronID() == 2));
    }

    @Test
    public void testGetPatronByEmail() throws SQLException {
        Patron patron = new Patron(1, "Alice Johnson", "alice.johnson@example.com");
        patronRepository.addPatron(patron);

        Patron retrievedPatron = patronRepository.getPatronByEmail("alice.johnson@example.com");
        assertNotNull(retrievedPatron);
        assertEquals(patron.getPatronID(), retrievedPatron.getPatronID());
        assertEquals(patron.getName(), retrievedPatron.getName());
    }
}

