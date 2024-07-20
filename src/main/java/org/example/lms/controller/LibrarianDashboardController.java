package org.example.lms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Book;
import org.example.lms.model.Librarian;
import org.example.lms.service.BookService;
import org.example.lms.service.TransactionService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

import java.util.List;

public class LibrarianDashboardController {
    @FXML
    private Button addBookButton;
    @FXML
    private Button manageTransactionsButton;
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

    private BookService bookService;

    public LibrarianDashboardController(BookService bookService, TransactionService transactionService) {
        this.bookService = bookService;
    }

    public void setLibrarian(Librarian librarian) {
        populateBooksTable();
    }

    @FXML
    private void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        addBookButton.setOnAction(e -> handleAddBook());
        manageTransactionsButton.setOnAction(e -> handleManageTransactions());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));
    }

    private void populateBooksTable() {
        booksTableView.setItems(bookService.getAllBooks());
    }

    private void handleAddBook() {
        // Code to add a new book
        showAlert(AlertType.INFORMATION, "Add Book", "Add book functionality is not implemented yet.");
    }

    private void handleManageTransactions() {
        // Code to manage transactions
        showAlert(AlertType.INFORMATION, "Manage Transactions", "Manage transactions functionality is not implemented yet.");
    }

    private void handleSearch(String query) {
        // Convert List to ObservableList
        List<Book> bookList = bookService.searchBooks(query);
        ObservableList<Book> observableBookList = FXCollections.observableArrayList(bookList);

        // Set items to the TableView
        booksTableView.setItems(observableBookList);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

