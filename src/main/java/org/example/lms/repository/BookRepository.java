package org.example.lms.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Book;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public static void setBookParameters(Book book, PreparedStatement statement) throws SQLException {
        statement.setString(1, book.getIsbn());
        statement.setString(2, book.getTitle());
        statement.setString(3, book.getAuthor());
        statement.setBoolean(4, book.isAvailable());
        statement.setInt(5, book.getLibraryId());
        statement.executeUpdate();
    }

    public void addBookToDatabase(Book book) {
        String query = "INSERT INTO Book (Isbn, Title, Author, IsAvailable, LibraryID) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setBookParameters(book, statement);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately (e.g., logging)
        }
    }

    public void updateBookInDatabase(Book book) {
        String query = "UPDATE Book SET Title = ?, Author = ?, IsAvailable = ? WHERE Isbn = ? AND LibraryID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setBookParameters(book, statement);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately (e.g., logging)
        }
    }

    public void deleteBookFromDatabase(String Isbn) {
        String query = "DELETE FROM Book WHERE Isbn = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Isbn);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately (e.g., logging)
        }
    }

    public Book getBookFromDatabase(String Isbn) {
        String query = "SELECT * FROM Book WHERE Isbn = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Isbn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Book(
                        resultSet.getString("Isbn"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getBoolean("IsAvailable"),
                        resultSet.getInt("LibraryID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately (e.g., logging)
        }
        return null;
    }

    // Get all books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Book";

        return getBooks(books, query);
    }

    // Get all available books
    public List<Book> findAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM Book WHERE IsAvailable = TRUE";

        return getBooks(books, query);
    }

    private List<Book> getBooks(List<Book> books, String query) {
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("Isbn"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getBoolean("IsAvailable"),
                        resultSet.getInt("LibraryID")
                );
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return books;
    }

    public ObservableList<Book> searchBooks(String query) {
        String sql = "SELECT * FROM Book WHERE Title LIKE ? OR Author LIKE ?";
        ObservableList<Book> books = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book(
                            resultSet.getString("Isbn"),
                            resultSet.getString("Title"),
                            resultSet.getString("Author"),
                            resultSet.getBoolean("IsAvailable"),
                            resultSet.getInt("LibraryID")
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

}
