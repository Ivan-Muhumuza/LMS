package org.example.lms.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.h2.jdbcx.JdbcDataSource;
import org.example.lms.model.Librarian;
import org.example.lms.repository.LibrarianRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

public class LibrarianRepositoryTest {

    private Connection connection;
    private LibrarianRepository librarianRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up H2 in-memory database
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"); // In-memory database URL
        dataSource.setUser("sa");
        dataSource.setPassword("");

        connection = dataSource.getConnection();
        librarianRepository = new LibrarianRepository();

        // Initialize the database schema
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Librarian (" +
                    "LibrarianID INT PRIMARY KEY, " +
                    "LibraryID INT, " +
                    "Name VARCHAR(255), " +
                    "Email VARCHAR(255), " +
                    "Password VARCHAR(255))");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up the database
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Librarian");
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testAddLibrarian() throws SQLException {
        Librarian librarian = new Librarian(1, 101, "John Doe", "john.doe@example.com", "password123");
        librarianRepository.addLibrarianToDatabase(librarian);

        Librarian retrievedLibrarian = librarianRepository.getLibrarianFromDatabase(1);
        assertNotNull(retrievedLibrarian);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
        assertEquals(librarian.getName(), retrievedLibrarian.getName());
        assertEquals(librarian.getEmail(), retrievedLibrarian.getEmail());
    }

    @Test
    public void testUpdateLibrarian() throws SQLException {
        Librarian librarian = new Librarian(1, 101, "John Doe", "john.doe@example.com", "password123");
        librarianRepository.addLibrarianToDatabase(librarian);

        librarian.setName("Jane Doe");
        librarian.setEmail("jane.doe@example.com");
        librarianRepository.updateLibrarianInDatabase(librarian);

        Librarian updatedLibrarian = librarianRepository.getLibrarianFromDatabase(1);
        assertNotNull(updatedLibrarian);
        assertEquals("Jane Doe", updatedLibrarian.getName());
        assertEquals("jane.doe@example.com", updatedLibrarian.getEmail());
    }

    @Test
    public void testDeleteLibrarian() throws SQLException {
        Librarian librarian = new Librarian(1, 101, "John Doe", "john.doe@example.com", "password123");
        librarianRepository.addLibrarianToDatabase(librarian);

        librarianRepository.deleteLibrarianFromDatabase(1);
        Librarian deletedLibrarian = librarianRepository.getLibrarianFromDatabase(1);
        assertNull(deletedLibrarian);
    }

    @Test
    public void testGetLibrarianByEmail() throws SQLException {
        Librarian librarian = new Librarian(1, 101, "John Doe", "john.doe@example.com", "password123");
        librarianRepository.addLibrarianToDatabase(librarian);

        Librarian retrievedLibrarian = librarianRepository.getLibrarianByEmail("john.doe@example.com");
        assertNotNull(retrievedLibrarian);
        assertEquals(librarian.getLibrarianID(), retrievedLibrarian.getLibrarianID());
        assertEquals(librarian.getName(), retrievedLibrarian.getName());
        assertEquals(librarian.getEmail(), retrievedLibrarian.getEmail());
    }
}

