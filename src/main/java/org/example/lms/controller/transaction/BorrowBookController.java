package org.example.lms.controller.transaction;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.lms.repository.BorrowedBookRepository;
import org.example.lms.service.BorrowedBookService;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BorrowBookController {
    @FXML
    private TextField patronIDField;
    @FXML
    private TextField isbnField;
    @FXML
    private DatePicker dueDatePicker;

    private BorrowedBookService borrowedBookService;

    @FXML
    private void initialize() throws SQLException {
        // Initialize with a database connection
        Connection connection = DatabaseUtil.getInstance().getConnection();
        this.borrowedBookService = new BorrowedBookService(connection);
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}