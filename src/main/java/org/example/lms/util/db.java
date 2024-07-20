//package org.example.lms.util;
//
//import model.*;
//import org.example.lms.model.*;
//import org.example.lms.repository.BookRepository;
//
//import java.sql.*;
//
//
//public class db {
//
////    private static final String URL = "jdbc:mysql://localhost:3306/library_schema";
////    private static final String USER = "root";
////    private static final String PASSWORD = "Pioneer4!";
////    private Connection connection;
////
////    public DatabaseUtil() throws SQLException {
////        connection = DriverManager.getConnection(URL, USER, PASSWORD);
////    }
//
////    public Connection getConnection() {
////        return connection;
////    }
//
//
//    // Methods for CRUD operations
//    public void addBookToDatabase(Book book) throws SQLException {
//        String query = "INSERT INTO Book (Isbn, Title, Author, IsAvailable, LibraryID) VALUES (?, ?, ?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        BookRepository.setBookParameters(book, statement);
//    }
//
//    public void updateBookInDatabase(Book book) throws SQLException {
//        String query = "UPDATE Book SET Title = ?, Author = ?, IsAvailable = ? WHERE Isbn = ? AND LibraryID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, book.getTitle());
//        statement.setString(2, book.getAuthor());
//        statement.setBoolean(3, book.isAvailable());
//        statement.setString(4, book.getIsbn());
//        statement.setInt(5,  book.getLibraryId());
//        statement.executeUpdate();
//    }
//
//    public void deleteBookFromDatabase(String Isbn) throws SQLException {
//        String query = "DELETE FROM Book WHERE Isbn = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, Isbn);
//        statement.executeUpdate();
//    }
//
//    public Book getBookFromDatabase(String Isbn) throws SQLException {
//        String query = "SELECT * FROM Book WHERE Isbn = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, Isbn);
//        ResultSet resultSet = statement.executeQuery();
//        if(resultSet.next()) {
//            return new Book(resultSet.getString("Isbn"), resultSet.getString("Title"), resultSet.getString("Author"), resultSet.getBoolean("IsAvailable"),  resultSet.getInt("LibraryID"));
//        }
//        return null;
//    }
//
//    // CRUD for Librarian
//    public void addLibrarianToDatabase(Librarian librarian) throws SQLException {
//        String query = "INSERT INTO Librarian (LibrarianID, LibraryID, Name, Email) VALUES (?, ?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, librarian.getLibrarianID());
//        statement.setInt(2, librarian.getLibraryID());
//        statement.setString(3, librarian.getName());
//        statement.setString(4, librarian.getEmail());
//        statement.executeUpdate();
//    }
//
//    public void updateLibrarianInDatabase(Librarian librarian) throws SQLException {
//        String query = "UPDATE Librarian SET LibraryID = ?, Name = ?, Email = ? WHERE LibrarianID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, librarian.getLibraryID());
//        statement.setString(2, librarian.getName());
//        statement.setString(3, librarian.getEmail());
//        statement.setInt(4, librarian.getLibrarianID());
//        statement.executeUpdate();
//    }
//
//    public void deleteLibrarianFromDatabase(int librarianID) throws SQLException {
//        String query = "DELETE FROM Librarian WHERE LibrarianID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, librarianID);
//        statement.executeUpdate();
//    }
//
//    public Librarian getLibrarianFromDatabase(int librarianID) throws SQLException {
//        String query = "SELECT * FROM Librarian WHERE LibrarianID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, librarianID);
//        ResultSet resultSet = statement.executeQuery();
//        if (resultSet.next()) {
//            return new Librarian(resultSet.getInt("LibrarianID"), resultSet.getInt("LibraryID"), resultSet.getString("Name"), resultSet.getString("Email"));
//        }
//        return null;
//    }
//
//    // CRUD for Patron
//    public void addPatronToDatabase(Patron patron) throws SQLException {
//        String query = "INSERT INTO Patron (PatronID, Name, Email) VALUES (?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, patron.getPatronID());
//        statement.setString(2, patron.getName());
//        statement.setString(3, patron.getEmail());
//        statement.executeUpdate();
//    }
//
//    public void updatePatronInDatabase(Patron patron) throws SQLException {
//        String query = "UPDATE Patron SET Name = ?, Email = ? WHERE PatronID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, patron.getName());
//        statement.setString(2, patron.getEmail());
//        statement.setInt(3, patron.getPatronID());
//        statement.executeUpdate();
//    }
//
//    public void deletePatronFromDatabase(int patronID) throws SQLException {
//        String query = "DELETE FROM Patron WHERE PatronID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, patronID);
//        statement.executeUpdate();
//    }
//
//    public Patron getPatronFromDatabase(int patronID) throws SQLException {
//        String query = "SELECT * FROM Patron WHERE PatronID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, patronID);
//        ResultSet resultSet = statement.executeQuery();
//        if (resultSet.next()) {
//            return new Patron(resultSet.getInt("PatronID"), resultSet.getString("Name"), resultSet.getString("Email"));
//        }
//        return null;
//    }
//
//    // CRUD for BorrowedBooks
//    public void addBorrowedBookToDatabase(BorrowedBook borrowedBook) throws SQLException {
//        String query = "INSERT INTO BorrowedBooks (PatronID, Isbn, BorrowedDate, DueDate) VALUES (?, ?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, borrowedBook.getPatronID());
//        statement.setString(2, borrowedBook.getIsbn());
//        statement.setTimestamp(3, Timestamp.valueOf(borrowedBook.getBorrowedDate()));
//        statement.setTimestamp(4, Timestamp.valueOf(borrowedBook.getDueDate()));
//        statement.executeUpdate();
//    }
//
//    public void updateBorrowedBookInDatabase(BorrowedBook borrowedBook) throws SQLException {
//        String query = "UPDATE BorrowedBooks SET BorrowedDate = ?, DueDate = ? WHERE PatronID = ? AND Isbn = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setTimestamp(1, Timestamp.valueOf(borrowedBook.getBorrowedDate()));
//        statement.setTimestamp(2, Timestamp.valueOf(borrowedBook.getDueDate()));
//        statement.setInt(3, borrowedBook.getPatronID());
//        statement.setString(4, borrowedBook.getIsbn());
//        statement.executeUpdate();
//    }
//
//    public void deleteBorrowedBookFromDatabase(int patronID, String isbn) throws SQLException {
//        String query = "DELETE FROM BorrowedBooks WHERE PatronID = ? AND Isbn = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, patronID);
//        statement.setString(2, isbn);
//        statement.executeUpdate();
//    }
//
//    public BorrowedBook getBorrowedBookFromDatabase(int patronID, String isbn) throws SQLException {
//        String query = "SELECT * FROM BorrowedBooks WHERE PatronID = ? AND Isbn = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, patronID);
//        statement.setString(2, isbn);
//        ResultSet resultSet = statement.executeQuery();
//        if (resultSet.next()) {
//            return new BorrowedBook(resultSet.getInt("PatronID"), resultSet.getString("Isbn"), resultSet.getTimestamp("BorrowedDate").toLocalDateTime(), resultSet.getTimestamp("DueDate").toLocalDateTime());
//        }
//        return null;
//    }
//
//    // CRUD for Transaction
//    public void addTransactionToDatabase(Transaction transaction) throws SQLException {
//        String query = "INSERT INTO Transaction (TransactionID, PatronID, BookIsbn, TransactionDate, DueDate, TypeID) VALUES (?, ?, ?, ?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, transaction.getTransactionID());
//        statement.setInt(2, transaction.getPatronID());
//        statement.setString(3, transaction.getBookIsbn());
//        statement.setTimestamp(4, Timestamp.valueOf(transaction.getTransactionDate()));
//        statement.setTimestamp(5, Timestamp.valueOf(transaction.getDueDate()));
//        statement.setInt(6, transaction.getTypeID());
//        statement.executeUpdate();
//    }
//
//    public void updateTransactionInDatabase(Transaction transaction) throws SQLException {
//        String query = "UPDATE Transaction SET PatronID = ?, BookIsbn = ?, TransactionDate = ?, DueDate = ?, TypeID = ? WHERE TransactionID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, transaction.getPatronID());
//        statement.setString(2, transaction.getBookIsbn());
//        statement.setTimestamp(3, Timestamp.valueOf(transaction.getTransactionDate()));
//        statement.setTimestamp(4, Timestamp.valueOf(transaction.getDueDate()));
//        statement.setInt(5, transaction.getTypeID());
//        statement.setInt(6, transaction.getTransactionID());
//        statement.executeUpdate();
//    }
//
//    public void deleteTransactionFromDatabase(int transactionID) throws SQLException {
//        String query = "DELETE FROM Transaction WHERE TransactionID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, transactionID);
//        statement.executeUpdate();
//    }
//
//    public Transaction getTransactionFromDatabase(int transactionID) throws SQLException {
//        String query = "SELECT * FROM Transaction WHERE TransactionID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, transactionID);
//        ResultSet resultSet = statement.executeQuery();
//        if (resultSet.next()) {
//            return new Transaction(resultSet.getInt("TransactionID"), resultSet.getInt("PatronID"), resultSet.getString("BookIsbn"), resultSet.getTimestamp("TransactionDate").toLocalDateTime(), resultSet.getTimestamp("DueDate").toLocalDateTime(), resultSet.getInt("TypeID"));
//        }
//        return null;
//    }
//
//    // CRUD for TransactionType
//    public void addTransactionTypeToDatabase(TransactionType transactionType) throws SQLException {
//        String query = "INSERT INTO TransactionType (TypeDescription) VALUES (?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, transactionType.getTypeDescription());
//        statement.executeUpdate();
//    }
//
//    public void updateTransactionTypeInDatabase(TransactionType transactionType) throws SQLException {
//        String query = "UPDATE TransactionType SET TypeDescription = ? WHERE TypeID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, transactionType.getTypeDescription());
//        statement.setInt(2, transactionType.getTypeID());
//        statement.executeUpdate();
//    }
//
//    public void deleteTransactionTypeFromDatabase(int typeID) throws SQLException {
//        String query = "DELETE FROM TransactionType WHERE TypeID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, typeID);
//        statement.executeUpdate();
//    }
//
//    public TransactionType getTransactionTypeFromDatabase(int typeID) throws SQLException {
//        String query = "SELECT * FROM TransactionType WHERE TypeID = ?";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, typeID);
//        ResultSet resultSet = statement.executeQuery();
//        if (resultSet.next()) {
//            return new TransactionType(resultSet.getInt("TypeID"), resultSet.getString("TypeDescription"));
//        }
//        return null;
//    }
//
//    // Close connection
//    public void close() throws SQLException {
//        if(connection != null) {
//            connection.close();
//        }
//    }
//}
