//package org.example.lms.service;
//
//import org.example.lms.model.Book;
//import org.example.lms.model.Patron;
//import org.example.lms.repository.LibraryRepository;
//
//import java.sql.SQLException;
//import java.util.LinkedList;
//
//public class LibraryService {
//
//    private final LibraryRepository libraryRepository;
//
//    public LibraryService() {
//        this.libraryRepository = new LibraryRepository();
//    }
//
//    public void addBook(Book book) throws SQLException {
//        // Validate input
//        if (book == null || book.getIsbn() == null || book.getIsbn().isEmpty()) {
//            throw new IllegalArgumentException("Invalid book data.");
//        }
//
//        // Add book to database
//        libraryRepository.addBookToDatabase(book);
//    }
//
//    public void removeBook(String isbn) throws SQLException {
//        // Validate input
//        if (isbn == null || isbn.isEmpty()) {
//            throw new IllegalArgumentException("Invalid ISBN.");
//        }
//
//        // Remove book from database
//        libraryRepository.removeBookFromDatabase(isbn);
//    }
//
//    public void addPatron(Patron patron) throws SQLException {
//        // Validate input
//        if (patron == null || patron.getPatronID() <= 0 || patron.getEmail() == null || patron.getEmail().isEmpty()) {
//            throw new IllegalArgumentException("Invalid patron data.");
//        }
//
//        // Add patron to database
//        libraryRepository.addPatronToDatabase(patron);
//    }
//
//    public void removePatron(int patronID) throws SQLException {
//        // Validate input
//        if (patronID <= 0) {
//            throw new IllegalArgumentException("Invalid Patron ID.");
//        }
//
//        // Remove patron from database
//        libraryRepository.removePatronFromDatabase(patronID);
//    }
//
//    public LinkedList<Book> getAllBooks() throws SQLException {
//        return libraryRepository.getAllBooks();
//    }
//
//    public LinkedList<Patron> getAllPatrons() throws SQLException {
//        return libraryRepository.getAllPatrons();
//    }
//}
//
