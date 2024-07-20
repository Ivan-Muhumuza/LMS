package org.example.lms.controller.book;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.lms.model.Book;
import org.example.lms.service.BookService;

public class AddBookController {

    @FXML
    private TextField isbnField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    private BookService bookService;

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @FXML
    private void handleAddBook() {
        try {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();

            Book newBook = new Book(isbn, title, author, true, 1);
            bookService.addBook(newBook);

            showAlert(Alert.AlertType.INFORMATION, "Book Added", "Book successfully added!");
            closeWindow();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) isbnField.getScene().getWindow();
        stage.close();
    }
}
