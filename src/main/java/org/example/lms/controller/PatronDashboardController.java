package org.example.lms.controller;

import org.example.lms.model.Book;
import org.example.lms.model.Patron;
import org.example.lms.service.BookService;
import org.example.lms.service.BorrowedBookService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

public class PatronDashboardController {
    @FXML
    private Button borrowBookButton;
    @FXML
    private Button returnBookButton;
    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TextField searchField;

    private Patron patron;
    private BookService bookService;
    private BorrowedBookService borrowedBookService;

    public PatronDashboardController(BookService bookService, BorrowedBookService borrowedBookService) {
        this.bookService = bookService;
        this.borrowedBookService = borrowedBookService;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
        populateBooksTable();
    }

    @FXML
    private void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        borrowBookButton.setOnAction(e -> handleBorrowBook());
        returnBookButton.setOnAction(e -> handleReturnBook());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));
    }

    private void populateBooksTable() {
        booksTableView.setItems(bookService.getAvailableBooks());
    }

    private void handleBorrowBook() {
        // Code to borrow a book
        showAlert(AlertType.INFORMATION, "Borrow Book", "Borrow book functionality is not implemented yet.");
    }

    private void handleReturnBook() {
        // Code to return a book
        showAlert(AlertType.INFORMATION, "Return Book", "Return book functionality is not implemented yet.");
    }

    private void handleSearch(String query) {
        booksTableView.setItems(bookService.searchBooks(query));
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

