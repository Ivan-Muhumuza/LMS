package org.example.lms.service;

import org.example.lms.model.BorrowedBook;
import org.example.lms.repository.BorrowedBookRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BorrowedBookService {
    private final BorrowedBookRepository borrowedBookRepository;

    public BorrowedBookService(Connection connection) {
        this.borrowedBookRepository = new BorrowedBookRepository(connection);
    }

    public void borrowBook(long patronID, String isbn, LocalDateTime borrowedDate, LocalDateTime dueDate) throws SQLException {
        BorrowedBook borrowedBook = new BorrowedBook(patronID, isbn, borrowedDate, dueDate);
        borrowedBookRepository.addBorrowedBook(borrowedBook);
    }

    public List<BorrowedBook> getBorrowedBooks(long patronID) throws SQLException {
        return borrowedBookRepository.getBorrowedBooksByPatron(patronID);
    }

    public void returnBook(long patronID, String isbn) throws SQLException {
        borrowedBookRepository.returnBook(patronID, isbn);
    }

    public List<BorrowedBook> getBorrowedBooksByPatron(long patronID) throws SQLException {
        return borrowedBookRepository.getBorrowedBooksByPatron(patronID);
    }
}
