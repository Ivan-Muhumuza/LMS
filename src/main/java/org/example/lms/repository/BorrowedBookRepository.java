package org.example.lms.repository;

import org.example.lms.model.BorrowedBook;
import org.example.lms.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBookRepository {
    public Connection connection;

    public BorrowedBookRepository(Connection connection) {
        this.connection = connection;
    }

    public void addBorrowedBook(BorrowedBook borrowedBook) throws SQLException {
        String insertQuery = "INSERT INTO BorrowedBooks (PatronID, Isbn, BorrowedDate, DueDate) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE Book SET IsAvailable = 0 WHERE Isbn = ?";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

            // Insert borrowed book record
            insertStmt.setLong(1, borrowedBook.getPatronID());
            insertStmt.setString(2, borrowedBook.getIsbn());
            insertStmt.setObject(3, borrowedBook.getBorrowedDate());
            insertStmt.setObject(4, borrowedBook.getDueDate());
            insertStmt.executeUpdate();

            // Update book availability
            updateStmt.setString(1, borrowedBook.getIsbn());
            updateStmt.executeUpdate();
        }
    }

    public BorrowedBook getBorrowedBook(String isbn) throws SQLException {
        String selectQuery = "SELECT PatronID, Isbn, BorrowedDate, DueDate FROM BorrowedBooks WHERE Isbn = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            selectStmt.setString(1, isbn);

            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    long patronID = resultSet.getLong("PatronID");
                    String bookIsbn = resultSet.getString("Isbn");
                    LocalDateTime borrowedDate = resultSet.getObject("BorrowedDate", LocalDateTime.class);
                    LocalDateTime dueDate = resultSet.getObject("DueDate", LocalDateTime.class);

                    return new BorrowedBook(patronID, bookIsbn, borrowedDate, dueDate);
                } else {
                    return null; // or throw an exception if book not found
                }
            }
        }
    }

    public void returnBook(long patronID, String isbn) throws SQLException {
        String deleteQuery = "DELETE FROM BorrowedBooks WHERE PatronID = ? AND Isbn = ?";
        String updateQuery = "UPDATE Book SET IsAvailable = 1 WHERE Isbn = ?";

        // Start transaction
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

            // Start transaction
            connection.setAutoCommit(false);

            // Delete entry from BorrowedBooks
            deleteStmt.setLong(1, patronID);
            deleteStmt.setString(2, isbn);
            deleteStmt.executeUpdate();

            // Update book availability
            updateStmt.setString(1, isbn);
            updateStmt.executeUpdate();

            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            // Rollback transaction on error
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                // Handle rollback exception
                rollbackEx.printStackTrace();
            }
            throw e;
        }
    }


    public List<BorrowedBook> getBorrowedBooksByPatron(long patronID) throws SQLException {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT * FROM BorrowedBooks WHERE PatronID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, patronID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                BorrowedBook borrowedBook = new BorrowedBook(
                        rs.getLong("PatronID"),
                        rs.getString("Isbn"),
                        rs.getObject("BorrowedDate", LocalDateTime.class),
                        rs.getObject("DueDate", LocalDateTime.class)
                );
                borrowedBooks.add(borrowedBook);
            }
        }
        return borrowedBooks;
    }

    public List<BorrowedBook> findAllBorrowedBooks() throws SQLException {
        String sql = "SELECT PatronID, Isbn, BorrowedDate, DueDate FROM BorrowedBooks";
        List<BorrowedBook> borrowedBooks = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long patronID = resultSet.getLong("PatronID");
                String isbn = resultSet.getString("Isbn");
                LocalDateTime borrowedDate = resultSet.getObject("BorrowedDate", LocalDateTime.class);
                LocalDateTime dueDate = resultSet.getObject("DueDate", LocalDateTime.class);

                BorrowedBook borrowedBook = new BorrowedBook(patronID, isbn, borrowedDate, dueDate);
                borrowedBooks.add(borrowedBook);
            }
        }

        return borrowedBooks;
    }

}
