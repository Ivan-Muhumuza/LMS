package org.example.lms.controller.transaction;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.example.lms.model.BorrowedBook;
import org.example.lms.service.BorrowedBookService;

public class ManageTransactionController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField patronIDField;
    @FXML
    private TextField isbnField;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Button borrowBookButton;
    @FXML
    private Button returnBookButton;
    @FXML
    private Button viewBorrowedBooksButton; // Add button to view borrowed books
    @FXML
    private BorrowedBookService borrowedBookService;

    public void setBorrowedBookService(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    @FXML
    private void handleBorrowBook() {
        try {
            long patronID = Long.parseLong(patronIDField.getText());
            String isbn = isbnField.getText();
            LocalDateTime borrowedDate = LocalDateTime.now();
            LocalDate dueDate = dueDatePicker.getValue();
            LocalDateTime dueDateTime = dueDate.atStartOfDay();

            borrowedBookService.borrowBook(patronID, isbn, borrowedDate, dueDateTime);
            showAlert(Alert.AlertType.INFORMATION, "Book Borrowed", "Book successfully borrowed!");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void handleReturnBook() {
        try {
            long patronID = Long.parseLong(patronIDField.getText());
            String isbn = isbnField.getText();

            borrowedBookService.returnBook(patronID, isbn);
            showAlert(Alert.AlertType.INFORMATION, "Book Returned", "Book successfully returned!");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void handleViewBorrowedBooks() {
        // Implementation for viewing borrowed books
        try {
            long patronID = Long.parseLong(patronIDField.getText());
            List<BorrowedBook> borrowedBooks = borrowedBookService.getBorrowedBooksByPatron(patronID);
            // You would typically update a TableView or similar UI component here
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
