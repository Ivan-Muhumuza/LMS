package org.example.lms.model;

import java.util.LinkedList;
import java.util.Stack;

public class Library {
    private LinkedList<Book> books;
    private LinkedList<Patron> patrons;
    private Stack<Transaction> transactions;

    public Library() {
        books = new LinkedList<>();
        patrons = new LinkedList<>();
        transactions = new Stack<>();
    }

    // Methods to add and remove books and patrons
    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void addPatron(Patron patron) {
        patrons.add(patron);
    }

    public void removePatron(Patron patron) {
        patrons.remove(patron);
    }

    // Helper methods to find books and patrons
    public Patron findPatronById(Integer PatronID) {
        for (Patron patron : patrons) {
            if (patron.getPatronID().equals(PatronID)) {
                return patron;
            }
        }
        return null;
    }

    public Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    public Stack<Transaction> getTransactions() {
        return transactions;
    }

    public LinkedList<Book> getBooks() {
        return books;
    }

    public LinkedList<Patron> getPatrons() {
        return patrons;
    }
}