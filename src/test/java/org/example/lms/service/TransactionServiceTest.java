package org.example.lms.service;

import org.example.lms.model.Transaction;
import org.example.lms.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    public void testAddTransactionSuccessfully() throws SQLException {
        Transaction transaction = new Transaction(1, 123456789, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14),1);

        // Spy on the service to mock the private method
        TransactionService spyService = spy(transactionService);
        doReturn(true).when(spyService).isBookAvailable(transaction.getBookIsbn());

        spyService.addTransaction(transaction);

        verify(transactionRepository, times(1)).addTransactionToDatabase(transaction);
    }

    @Test
    public void testAddTransactionThrowsExceptionWhenBookNotAvailable() throws SQLException {
        Transaction transaction = new Transaction(1, 123456789, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14),1);

        // Spy on the service to mock the private method
        TransactionService spyService = spy(transactionService);
        doReturn(false).when(spyService).isBookAvailable(transaction.getBookIsbn());

        assertThrows(IllegalArgumentException.class, () -> spyService.addTransaction(transaction));

        verify(transactionRepository, times(0)).addTransactionToDatabase(transaction);
    }

    @Test
    public void testUpdateTransactionSuccessfully() throws SQLException {
        Transaction transaction = new Transaction(1, 123456789, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14),1);
        when(transactionRepository.getTransactionFromDatabase(transaction.getTransactionID())).thenReturn(transaction);

        transactionService.updateTransaction(transaction);

        verify(transactionRepository, times(1)).updateTransactionInDatabase(transaction);
    }

    @Test
    public void testUpdateTransactionThrowsExceptionWhenTransactionNotFound() throws SQLException {
        Transaction transaction = new Transaction(1, 123456789, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14),1);
        when(transactionRepository.getTransactionFromDatabase(transaction.getTransactionID())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transactionService.updateTransaction(transaction));

        verify(transactionRepository, times(0)).updateTransactionInDatabase(transaction);
    }

    @Test
    public void testDeleteTransactionSuccessfully() throws SQLException {
        Transaction transaction = new Transaction(1, 123456789, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14),1);
        when(transactionRepository.getTransactionFromDatabase(transaction.getTransactionID())).thenReturn(transaction);

        transactionService.deleteTransaction(transaction.getTransactionID());

        verify(transactionRepository, times(1)).deleteTransactionFromDatabase(transaction.getTransactionID());
    }

    @Test
    public void testDeleteTransactionThrowsExceptionWhenTransactionNotFound() throws SQLException {
        int transactionID = 1;
        when(transactionRepository.getTransactionFromDatabase(transactionID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transactionService.deleteTransaction(transactionID));

        verify(transactionRepository, times(0)).deleteTransactionFromDatabase(transactionID);
    }

    @Test
    public void testGetTransactionSuccessfully() throws SQLException {
        Transaction transaction = new Transaction(1, 123456789, "123456789", LocalDateTime.now(), LocalDateTime.now().plusDays(14),1);
        when(transactionRepository.getTransactionFromDatabase(transaction.getTransactionID())).thenReturn(transaction);

        Transaction result = transactionService.getTransaction(transaction.getTransactionID());

        assertNotNull(result);
        assertEquals(transaction, result);
    }

    @Test
    public void testGetTransactionThrowsExceptionWhenTransactionNotFound() throws SQLException {
        int transactionID = 1;
        when(transactionRepository.getTransactionFromDatabase(transactionID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransaction(transactionID));
    }
}