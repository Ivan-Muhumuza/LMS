package org.example.lms.repository;

import org.example.lms.model.BorrowedBook;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBookRepository {
    private Connection connection;

    public BorrowedBookRepository(Connection connection) {
        this.connection = connection;
    }

    public void addBorrowedBook(BorrowedBook borrowedBook) throws SQLException {
        String query = "INSERT INTO BorrowedBooks (PatronID, Isbn, BorrowedDate, DueDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, borrowedBook.getPatronID());
            statement.setString(2, borrowedBook.getIsbn());
            statement.setObject(3, borrowedBook.getBorrowedDate());
            statement.setObject(4, borrowedBook.getDueDate());
            statement.executeUpdate();
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

    public void returnBook(long patronID, String isbn) throws SQLException {
        String query = "DELETE FROM BorrowedBooks WHERE PatronID = ? AND Isbn = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, patronID);
            statement.setString(2, isbn);
            statement.executeUpdate();
        }
    }
}
