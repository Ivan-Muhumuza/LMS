package org.example.lms.controller.book;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.lms.model.Book;
import org.example.lms.service.BookService;

public class EditBookController {

    @FXML
    private TextField isbnField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    private BookService bookService;
    private Book book;

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setBook(Book book) {
        this.book = book;
        populateBookDetails();
    }

    private void populateBookDetails() {
        if (book != null) {
            isbnField.setText(book.getIsbn());
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
        }
    }

    @FXML
    private void handleEditBook() {
        try {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();

            Book updatedBook = new Book(isbn, title, author, book.isAvailable(), book.getLibraryID());
            bookService.updateBook(updatedBook);

            showAlert(Alert.AlertType.INFORMATION, "Book Updated", "Book successfully updated!");
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

