package org.example.lms.service;

import org.example.lms.model.Transaction;
import org.example.lms.repository.TransactionRepository;

import java.sql.SQLException;

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        // Validate input
        validateTransaction(transaction);

        // Check if the book is available (implement your logic here)
        if (!isBookAvailable(transaction.getBookIsbn())) {
            throw new IllegalArgumentException("Book is not available for borrowing.");
        }

        // Add transaction to the database
        transactionRepository.addTransactionToDatabase(transaction);
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        // Validate input
        validateTransaction(transaction);

        // Optional: Check if the transaction exists before updating
        Transaction existingTransaction = transactionRepository.getTransactionFromDatabase(transaction.getTransactionID());
        if (existingTransaction == null) {
            throw new IllegalArgumentException("Transaction not found.");
        }

        // Update transaction in the database
        transactionRepository.updateTransactionInDatabase(transaction);
    }

    public void deleteTransaction(int transactionID) throws SQLException {
        // Check if the transaction exists before deleting
        Transaction existingTransaction = transactionRepository.getTransactionFromDatabase(transactionID);
        if (existingTransaction == null) {
            throw new IllegalArgumentException("Transaction not found.");
        }

        // Delete transaction from the database
        transactionRepository.deleteTransactionFromDatabase(transactionID);
    }

    public Transaction getTransaction(int transactionID) throws SQLException {
        // Fetch the transaction from the database
        Transaction transaction = transactionRepository.getTransactionFromDatabase(transactionID);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found.");
        }
        return transaction;
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getPatronID() <= 0) {
            throw new IllegalArgumentException("Invalid Patron ID.");
        }
        if (transaction.getBookIsbn() == null || transaction.getBookIsbn().isEmpty()) {
            throw new IllegalArgumentException("Book ISBN cannot be null or empty.");
        }
        if (transaction.getTypeID() <= 0) {
            throw new IllegalArgumentException("Invalid Transaction Type ID.");
        }
        if (transaction.getTransactionDate() == null || transaction.getDueDate() == null) {
            throw new IllegalArgumentException("Transaction date and due date cannot be null.");
        }
    }

    private boolean isBookAvailable(String bookIsbn) {
        // Implement logic to check if the book is available
        // This might involve querying the database or another repository
        return true; // Placeholder
    }
}

