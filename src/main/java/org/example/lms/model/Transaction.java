package org.example.lms.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private int TransactionID;
    private int PatronID;
    private String BookIsbn;
    private LocalDateTime TransactionDate;
    private LocalDateTime DueDate;
    private int TypeID;

    public Transaction(int transactionID, int patronID, String bookIsbn, LocalDateTime transactionDate, LocalDateTime dueDate, int typeID) {
        TransactionID = transactionID;
        PatronID = patronID;
        BookIsbn = bookIsbn;
        TransactionDate = transactionDate.truncatedTo(ChronoUnit.SECONDS);
        DueDate = dueDate.truncatedTo(ChronoUnit.SECONDS);
        TypeID = typeID;
    }

    public int getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(int transactionID) {
        TransactionID = transactionID;
    }

    public int getPatronID() {
        return PatronID;
    }

    public void setPatronID(int patronID) {
        PatronID = patronID;
    }

    public String getBookIsbn() {
        return BookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        BookIsbn = bookIsbn;
    }

    public LocalDateTime getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        TransactionDate = transactionDate;
    }

    public LocalDateTime getDueDate() {
        return DueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        DueDate = dueDate;
    }

    public int getTypeID() {
        return TypeID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }
}
