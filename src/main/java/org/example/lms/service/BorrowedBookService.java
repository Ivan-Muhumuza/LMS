package org.example.lms.service;

import org.example.lms.model.BorrowedBook;
import org.example.lms.repository.BorrowedBookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BorrowedBookService {
    private final BorrowedBookRepository borrowedBookRepository;

    public BorrowedBookService(Connection connection) {
        this.borrowedBookRepository = new BorrowedBookRepository(connection);
    }


    public void borrowBook(long patronID, String isbn, LocalDateTime borrowedDate, LocalDateTime dueDate) throws SQLException {
        // Check if the book is available
        String checkQuery = "SELECT IsAvailable FROM Book WHERE Isbn = ?";

        try (PreparedStatement checkStmt = borrowedBookRepository.connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, isbn);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("IsAvailable") == 1) {
                // Book is available, proceed with borrowing
                BorrowedBook borrowedBook = new BorrowedBook(patronID, isbn, borrowedDate, dueDate);
                borrowedBookRepository.addBorrowedBook(borrowedBook);
            } else {
                // Handle book not available case
                throw new IllegalStateException("The book is currently not available.");
            }
        }
    }

    public void returnBook(long patronID, String isbn) throws SQLException {
        borrowedBookRepository.returnBook(patronID, isbn);
    }

    public List<BorrowedBook> getBorrowedBooks(long patronID) throws SQLException {
        return borrowedBookRepository.getBorrowedBooksByPatron(patronID);
    }

    public List<BorrowedBook> getAllBorrowedBooks() throws SQLException {
        return borrowedBookRepository.findAllBorrowedBooks();
    }

    public List<BorrowedBook> getBorrowedBooksByPatron(long patronID) throws SQLException {
        return borrowedBookRepository.getBorrowedBooksByPatron(patronID);
    }
}
