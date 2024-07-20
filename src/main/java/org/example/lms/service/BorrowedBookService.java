package org.example.lms.service;

import org.example.lms.model.BorrowedBook;
import org.example.lms.repository.BorrowedBookRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class BorrowedBookService {

    private final BorrowedBookRepository borrowedBookRepository;

    public BorrowedBookService() {
        this.borrowedBookRepository = new BorrowedBookRepository();
    }

    public void addBorrowedBook(BorrowedBook borrowedBook) throws SQLException {
        // Validate input
        if (borrowedBook == null) {
            throw new IllegalArgumentException("BorrowedBook cannot be null.");
        }
        if (borrowedBook.getPatronID() <= 0) {
            throw new IllegalArgumentException("Invalid Patron ID.");
        }
        if (borrowedBook.getIsbn() == null || borrowedBook.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        if (borrowedBook.getBorrowedDate() == null || borrowedBook.getDueDate() == null) {
            throw new IllegalArgumentException("Borrowed and Due dates cannot be null.");
        }
        if (borrowedBook.getDueDate().isBefore(borrowedBook.getBorrowedDate())) {
            throw new IllegalArgumentException("Due date cannot be before Borrowed date.");
        }

        // Proceed to add the borrowed book to the database
        borrowedBookRepository.addBorrowedBookToDatabase(borrowedBook);
    }

    public void updateBorrowedBook(BorrowedBook borrowedBook) throws SQLException {
        // Validate input
        if (borrowedBook == null) {
            throw new IllegalArgumentException("BorrowedBook cannot be null.");
        }
        if (borrowedBook.getPatronID() <= 0) {
            throw new IllegalArgumentException("Invalid Patron ID.");
        }
        if (borrowedBook.getIsbn() == null || borrowedBook.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        if (borrowedBook.getBorrowedDate() == null || borrowedBook.getDueDate() == null) {
            throw new IllegalArgumentException("Borrowed and Due dates cannot be null.");
        }
        if (borrowedBook.getDueDate().isBefore(borrowedBook.getBorrowedDate())) {
            throw new IllegalArgumentException("Due date cannot be before Borrowed date.");
        }

        // Update the borrowed book in the database
        borrowedBookRepository.updateBorrowedBookInDatabase(borrowedBook);
    }

    public void deleteBorrowedBook(int patronID, String isbn) throws SQLException {
        // Validate input
        if (patronID <= 0) {
            throw new IllegalArgumentException("Invalid Patron ID.");
        }
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        // Delete the borrowed book from the database
        borrowedBookRepository.deleteBorrowedBookFromDatabase(patronID, isbn);
    }

    public BorrowedBook getBorrowedBook(int patronID, String isbn) throws SQLException {
        // Validate input
        if (patronID <= 0) {
            throw new IllegalArgumentException("Invalid Patron ID.");
        }
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        // Fetch the borrowed book from the database
        return borrowedBookRepository.getBorrowedBookFromDatabase(patronID, isbn);
    }
}

