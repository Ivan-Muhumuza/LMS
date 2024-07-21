package org.example.lms.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BorrowedBook {
    private long PatronID;
    private String Isbn;
    private LocalDateTime BorrowedDate;
    private LocalDateTime DueDate;

    public BorrowedBook(long patronID, String isbn, LocalDateTime borrowedDate, LocalDateTime dueDate) {
        this.PatronID = patronID;
        this.Isbn = isbn;
        this.BorrowedDate = borrowedDate;
        this.DueDate = dueDate;
    }

    public long getPatronID() {
        return PatronID;
    }

    public void setPatronID(long patronID) {
        this.PatronID = patronID;
    }

    public String getIsbn() {
        return Isbn;
    }

    public void setIsbn(String isbn) {
        this.Isbn = isbn;
    }

    public LocalDateTime getBorrowedDate() {
        return BorrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.BorrowedDate = borrowedDate;
    }

    public LocalDateTime getDueDate() {
        return DueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.DueDate = dueDate;
    }
}