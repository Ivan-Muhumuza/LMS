package views;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BorrowedBook {
    private int PatronID;
    private String Isbn;
    private LocalDateTime BorrowedDate;
    private LocalDateTime DueDate;

    public BorrowedBook(int patronID, String isbn, LocalDateTime borrowedDate, LocalDateTime dueDate) {
        PatronID = patronID;
        Isbn = isbn;
        BorrowedDate = borrowedDate.truncatedTo(ChronoUnit.SECONDS);
        DueDate = dueDate.truncatedTo(ChronoUnit.SECONDS);
    }

    public int getPatronID() {
        return PatronID;
    }

    public void setPatronID(int patronID) {
        PatronID = patronID;
    }

    public String getIsbn() {
        return Isbn;
    }

    public void setIsbn(String isbn) {
        Isbn = isbn;
    }

    public LocalDateTime getBorrowedDate() {
        return BorrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        BorrowedDate = borrowedDate;
    }

    public LocalDateTime getDueDate() {
        return DueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        DueDate = dueDate;
    }
}
