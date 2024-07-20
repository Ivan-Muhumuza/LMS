package org.example.lms.repository;

import org.example.lms.model.BorrowedBook;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BorrowedBookRepository {

    public void addBorrowedBookToDatabase(BorrowedBook borrowedBook) throws SQLException {
        String query = "INSERT INTO BorrowedBook (PatronID, Isbn, BorrowedDate, DueDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowedBook.getPatronID());
            statement.setString(2, borrowedBook.getIsbn());
            statement.setTimestamp(3, Timestamp.valueOf(borrowedBook.getBorrowedDate()));
            statement.setTimestamp(4, Timestamp.valueOf(borrowedBook.getDueDate()));
            statement.executeUpdate();
        }
    }

    public void updateBorrowedBookInDatabase(BorrowedBook borrowedBook) throws SQLException {
        String query = "UPDATE BorrowedBook SET BorrowedDate = ?, DueDate = ? WHERE PatronID = ? AND Isbn = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(borrowedBook.getBorrowedDate()));
            statement.setTimestamp(2, Timestamp.valueOf(borrowedBook.getDueDate()));
            statement.setInt(3, borrowedBook.getPatronID());
            statement.setString(4, borrowedBook.getIsbn());
            statement.executeUpdate();
        }
    }

    public void deleteBorrowedBookFromDatabase(int patronID, String isbn) throws SQLException {
        String query = "DELETE FROM BorrowedBook WHERE PatronID = ? AND Isbn = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronID);
            statement.setString(2, isbn);
            statement.executeUpdate();
        }
    }

    public BorrowedBook getBorrowedBookFromDatabase(int patronID, String isbn) throws SQLException {
        String query = "SELECT * FROM BorrowedBook WHERE PatronID = ? AND Isbn = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronID);
            statement.setString(2, isbn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new BorrowedBook(
                        resultSet.getInt("PatronID"),
                        resultSet.getString("Isbn"),
                        resultSet.getTimestamp("BorrowedDate").toLocalDateTime(),
                        resultSet.getTimestamp("DueDate").toLocalDateTime()
                );
            }
            return null;
        }
    }
}
