package org.example.lms.service;

import org.example.lms.model.Transaction;
import org.example.lms.repository.TransactionRepository;
import org.example.lms.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void testAddTransaction() throws SQLException {
//        Transaction transaction = new Transaction(1, 12345, "1", LocalDateTime.now(), LocalDateTime.now().plusDays(14),2);
//
//        when(transactionService.isBookAvailable("12345")).thenReturn(true);
//
//        transactionService.addTransaction(transaction);
//
//        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
//        verify(transactionRepository, times(1)).addTransactionToDatabase(transactionCaptor.capture());
//        assertEquals(transaction, transactionCaptor.getValue());
    }

    @Test
    void testUpdateTransaction() throws SQLException {
        Transaction transaction = new Transaction(1, 12345, "1", LocalDateTime.now(), LocalDateTime.now().plusDays(14),2);

        when(transactionRepository.getTransactionFromDatabase(1)).thenReturn(transaction);

        transactionService.updateTransaction(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).updateTransactionInDatabase(transactionCaptor.capture());
        assertEquals(transaction, transactionCaptor.getValue());
    }


    @Test
    void testDeleteTransaction() throws SQLException {
        Transaction transaction = new Transaction(1, 12345, "1", LocalDateTime.now(), LocalDateTime.now().plusDays(14),2);

        when(transactionRepository.getTransactionFromDatabase(1)).thenReturn(transaction);

        transactionService.deleteTransaction(1);

        verify(transactionRepository, times(1)).deleteTransactionFromDatabase(1);
    }

    @Test
    void testGetTransaction() throws SQLException {
        Transaction transaction = new Transaction(1, 12345, "1", LocalDateTime.now(), LocalDateTime.now().plusDays(14),2);

        when(transactionRepository.getTransactionFromDatabase(1)).thenReturn(transaction);

        Transaction result = transactionService.getTransaction(1);

        assertEquals(transaction, result);
    }

}
