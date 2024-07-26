package org.example.lms.repository;

import org.example.lms.model.Transaction;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void addTransactionToDatabase(Transaction transaction) throws SQLException {
        String query = "INSERT INTO Transaction (TransactionID, PatronID, BookIsbn, TransactionDate, DueDate, TypeID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaction.getTransactionID());
            statement.setInt(2, transaction.getPatronID());
            statement.setString(3, transaction.getBookIsbn());
            statement.setTimestamp(4, Timestamp.valueOf(transaction.getTransactionDate()));
            statement.setTimestamp(5, Timestamp.valueOf(transaction.getDueDate()));
            statement.setInt(6, transaction.getTypeID());
            statement.executeUpdate();
        }
    }

    public void updateTransactionInDatabase(Transaction transaction) throws SQLException {
        String query = "UPDATE Transaction SET PatronID = ?, BookIsbn = ?, TransactionDate = ?, DueDate = ?, TypeID = ? WHERE TransactionID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaction.getPatronID());
            statement.setString(2, transaction.getBookIsbn());
            statement.setTimestamp(3, Timestamp.valueOf(transaction.getTransactionDate()));
            statement.setTimestamp(4, Timestamp.valueOf(transaction.getDueDate()));
            statement.setInt(5, transaction.getTypeID());
            statement.setInt(6, transaction.getTransactionID());
            statement.executeUpdate();
        }
    }

    public void deleteTransactionFromDatabase(int transactionID) throws SQLException {
        String query = "DELETE FROM Transaction WHERE TransactionID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transactionID);
            statement.executeUpdate();
        }
    }

    public Transaction getTransactionFromDatabase(int transactionID) throws SQLException {
        String query = "SELECT * FROM Transaction WHERE TransactionID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transactionID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Transaction(
                        resultSet.getInt("TransactionID"),
                        resultSet.getInt("PatronID"),
                        resultSet.getString("BookIsbn"),
                        resultSet.getTimestamp("TransactionDate").toLocalDateTime(),
                        resultSet.getTimestamp("DueDate").toLocalDateTime(),
                        resultSet.getInt("TypeID")
                );
            }
            return null;
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        String query = "SELECT * FROM Transaction";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            List<Transaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("TransactionID"),
                        resultSet.getInt("PatronID"),
                        resultSet.getString("BookIsbn"),
                        resultSet.getTimestamp("TransactionDate").toLocalDateTime(),
                        resultSet.getTimestamp("DueDate").toLocalDateTime(),
                        resultSet.getInt("TypeID")
                ));
            }
            return transactions;
        }
    }
}
