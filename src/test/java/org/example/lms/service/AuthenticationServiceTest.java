package org.example.lms.service;

import org.example.lms.model.Librarian;
import org.example.lms.repository.LibrarianRepository;
import org.example.lms.repository.PatronRepository;
import org.example.lms.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private LibrarianRepository librarianRepository;

    @Mock PatronRepository patronRepository;
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(librarianRepository, patronRepository);
    }

    @Test
    public void testAuthenticateLibrarianSuccessfully() {
        String email = "librarian@example.com";
        String password = "password123";
        String hashedPassword = "hashedPassword123";
        Librarian librarian = new Librarian(1, 101, "John Mukasa", email, hashedPassword);

        when(librarianRepository.getLibrarianByEmail(email)).thenReturn(librarian);

        try (MockedStatic<DatabaseUtil> mockedDatabaseUtil = mockStatic(DatabaseUtil.class)) {
            mockedDatabaseUtil.when(() -> DatabaseUtil.checkPassword(password, hashedPassword)).thenReturn(true);

            Librarian result = authenticationService.authenticateLibrarian(email, password);

            assertEquals(librarian, result, "Librarian should be authenticated successfully.");
        }
    }

    @Test
    public void testAuthenticateLibrarianFailsWithIncorrectPassword() {
        String email = "librarian@example.com";
        String password = "password123";
        String hashedPassword = "hashedPassword123";
        Librarian librarian = new Librarian(1, 101, "John Mukasa", email, hashedPassword);

        when(librarianRepository.getLibrarianByEmail(email)).thenReturn(librarian);

        try (MockedStatic<DatabaseUtil> mockedDatabaseUtil = mockStatic(DatabaseUtil.class)) {
            mockedDatabaseUtil.when(() -> DatabaseUtil.checkPassword(password, hashedPassword)).thenReturn(false);

            Librarian result = authenticationService.authenticateLibrarian(email, password);

            assertNull(result, "Librarian should not be authenticated with incorrect password.");
        }
    }

    @Test
    public void testAuthenticateLibrarianFailsWithNonExistentEmail() {
        String email = "nonexistent@example.com";
        String password = "password123";

        when(librarianRepository.getLibrarianByEmail(email)).thenReturn(null);

        Librarian result = authenticationService.authenticateLibrarian(email, password);

        assertNull(result, "Librarian should not be authenticated with non-existent email.");
    }
}