package org.example.lms.service;

import org.example.lms.model.BorrowedBook;
import org.example.lms.repository.BorrowedBookRepository;
import org.example.lms.service.BorrowedBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowedBookServiceTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private BorrowedBookRepository borrowedBookRepository;

    private BorrowedBookService borrowedBookService;

    @BeforeEach
    void setUp() throws SQLException {
        borrowedBookRepository = new BorrowedBookRepository(connection);
        borrowedBookService = new BorrowedBookService(connection);
    }

    @Test
    void testBorrowBook() throws SQLException {
        // Setup
        long patronID = 1L;
        String isbn = "12345";
        LocalDateTime borrowedDate = LocalDateTime.now();
        LocalDateTime dueDate = borrowedDate.plusDays(14);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("IsAvailable")).thenReturn(1);

        // Act
        borrowedBookService.borrowBook(patronID, isbn, borrowedDate, dueDate);

        // Verify
        verify(borrowedBookRepository, times(1)).addBorrowedBook(any(BorrowedBook.class));
    }



    @Test
    void testReturnBook() throws SQLException {
        long patronID = 1L;
        String isbn = "12345";

        borrowedBookService.returnBook(patronID, isbn);

        verify(borrowedBookRepository, times(1)).returnBook(patronID, isbn);
    }

    @Test
    void testGetBorrowedBooks() throws SQLException {
        long patronID = 1L;
        List<BorrowedBook> expectedBooks = Arrays.asList(
                new BorrowedBook(patronID, "12345", LocalDateTime.now(), LocalDateTime.now().plusDays(14)),
                new BorrowedBook(patronID, "67890", LocalDateTime.now(), LocalDateTime.now().plusDays(14))
        );

        when(borrowedBookRepository.getBorrowedBooksByPatron(patronID)).thenReturn(expectedBooks);

        List<BorrowedBook> actualBooks = borrowedBookService.getBorrowedBooks(patronID);

        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void testGetAllBorrowedBooks() throws SQLException {
        List<BorrowedBook> expectedBooks = Arrays.asList(
                new BorrowedBook(1L, "12345", LocalDateTime.now(), LocalDateTime.now().plusDays(14)),
                new BorrowedBook(2L, "67890", LocalDateTime.now(), LocalDateTime.now().plusDays(14))
        );

        when(borrowedBookRepository.findAllBorrowedBooks()).thenReturn(expectedBooks);

        List<BorrowedBook> actualBooks = borrowedBookService.getAllBorrowedBooks();

        assertEquals(expectedBooks, actualBooks);
    }
}
