package org.example.lms.repository;

import org.example.lms.model.Book;
import org.example.lms.model.Patron;
import org.example.lms.util.DatabaseUtil;

import java.sql.*;
import java.util.LinkedList;

public class LibraryRepository {

    public void addBookToDatabase(Book book) throws SQLException {
        String query = "INSERT INTO Book (Isbn, Title, Author, IsAvailable, LibraryID) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setBoolean(4, book.isAvailable());
            statement.setInt(5, book.getLibraryID());
            statement.executeUpdate();
        }
    }

    public void removeBookFromDatabase(String isbn) throws SQLException {
        String query = "DELETE FROM Book WHERE Isbn = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        }
    }

    public void addPatronToDatabase(Patron patron) throws SQLException {
        String query = "INSERT INTO Patron (PatronID, Name, Email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patron.getPatronID());
            statement.setString(2, patron.getName());
            statement.setString(3, patron.getEmail());
            statement.executeUpdate();
        }
    }

    public void removePatronFromDatabase(int patronID) throws SQLException {
        String query = "DELETE FROM Patron WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronID);
            statement.executeUpdate();
        }
    }

    public LinkedList<Book> getAllBooks() throws SQLException {
        LinkedList<Book> books = new LinkedList<>();
        String query = "SELECT * FROM Book";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                books.add(new Book(resultSet.getString("Isbn"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getBoolean("IsAvailable"),
                        resultSet.getInt("LibraryID")));
            }
        }
        return books;
    }

    public LinkedList<Patron> getAllPatrons() throws SQLException {
        LinkedList<Patron> patrons = new LinkedList<>();
        String query = "SELECT * FROM Patron";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                patrons.add(new Patron(resultSet.getInt("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email")));

            }
        }
        return patrons;
    }
}

