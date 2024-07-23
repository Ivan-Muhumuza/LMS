package org.example.lms.service;

import org.example.lms.model.BorrowedBook;
import org.example.lms.repository.BorrowedBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BorrowedBookServiceTest {

    private BorrowedBookRepository borrowedBookRepository;
    private BorrowedBookService borrowedBookService;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {
        borrowedBookRepository = Mockito.mock(BorrowedBookRepository.class);
        borrowedBookService = new BorrowedBookService(borrowedBookRepository);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        borrowedBookRepository.connection = mockConnection;
    }

    @Test
    public void testBorrowBookSuccessfully() throws SQLException {
        String isbn = "123456789";
        long patronID = 1;
        LocalDateTime borrowedDate = LocalDateTime.now();
        LocalDateTime dueDate = borrowedDate.plusDays(14);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("IsAvailable")).thenReturn(1);

        borrowedBookService.borrowBook(patronID, isbn, borrowedDate, dueDate);

        verify(borrowedBookRepository, times(1)).addBorrowedBook(any(BorrowedBook.class));
    }

    @Test
    public void testBorrowBookThrowsExceptionWhenBookNotAvailable() throws SQLException {
        String isbn = "123456789";
        long patronID = 1;
        LocalDateTime borrowedDate = LocalDateTime.now();
        LocalDateTime dueDate = borrowedDate.plusDays(14);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("IsAvailable")).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> borrowedBookService.borrowBook(patronID, isbn, borrowedDate, dueDate));

        verify(borrowedBookRepository, times(0)).addBorrowedBook(any(BorrowedBook.class));
    }

    @Test
    public void testReturnBookSuccessfully() throws SQLException {
        String isbn = "123456789";
        long patronID = 1;

        borrowedBookService.returnBook(patronID, isbn);

        verify(borrowedBookRepository, times(1)).returnBook(patronID, isbn);
    }

    @Test
    public void testGetBorrowedBooks() throws SQLException {
        long patronID = 1;
        BorrowedBook book1 = new BorrowedBook(patronID, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        BorrowedBook book2 = new BorrowedBook(patronID, "987654321", LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        List<BorrowedBook> borrowedBooks = Arrays.asList(book1, book2);

        when(borrowedBookRepository.getBorrowedBooksByPatron(patronID)).thenReturn(borrowedBooks);

        List<BorrowedBook> result = borrowedBookService.getBorrowedBooks(patronID);

        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
    }

    @Test
    public void testGetAllBorrowedBooks() throws SQLException {
        BorrowedBook book1 = new BorrowedBook(1, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        BorrowedBook book2 = new BorrowedBook(2, "987654321", LocalDateTime.now(), LocalDateTime.now().plusDays(14));
        List<BorrowedBook> borrowedBooks = Arrays.asList(book1, book2);

        when(borrowedBookRepository.findAllBorrowedBooks()).thenReturn(borrowedBooks);

        List<BorrowedBook> result = borrowedBookService.getAllBorrowedBooks();

        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
    }
}
