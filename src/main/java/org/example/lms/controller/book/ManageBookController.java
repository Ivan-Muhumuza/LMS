package org.example.lms.controller.book;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.lms.controller.LibrarianDashboardController;
import org.example.lms.model.Book;
import org.example.lms.service.BookService;
import org.example.lms.service.PatronService;

import java.util.List;

import java.io.IOException;

public class ManageBookController {

    // No-argument constructor
    public ManageBookController() {
    }

    @FXML
    private TextField searchBookField;

    private static BookService bookService;
    private PatronService patronService;

    @FXML
    private TableView<Book> booksTableView;

    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private Button backToDashboardButton;
    @FXML
    private Button editBookButton;
    @FXML
    private Button deleteBookButton;

    // Method to set BookService and populate the table
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @FXML
    private void initialize() {
        // Set cell value factories
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("Isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));

        // Add search field listener
        searchBookField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));

        // Set button actions
        editBookButton.setOnAction(e -> handleEditBook());
        deleteBookButton.setOnAction(e -> handleDeleteBook());
        backToDashboardButton.setOnAction(e -> handleBackToDashboard());

        // Populate the table when the scene is initialized
        populateBooksTable();
    }

    public void populateBooksTable() {
        if (bookService != null) {
            booksTableView.setItems(bookService.getAllBooks());
//            booksTableView.setItems(FXCollections.observableArrayList(bookService.getAllBooks()));
        }
    }

    private void handleSearch(String query) {
        if (bookService != null) {
            List<Book> filteredBooks = bookService.searchBooks(query);
            booksTableView.setItems(FXCollections.observableArrayList(filteredBooks));
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/librarian/librarian_dashboard.fxml"));
            Parent root = loader.load();

            LibrarianDashboardController controller = loader.getController();
            controller.setBookService(bookService); // Ensure BookService is set again

            Stage stage = (Stage) backToDashboardButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Dashboard");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the dashboard scene.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/book/add_book_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Book");
            stage.setScene(new Scene(root));

            // Pass BookService to the controller
            AddBookController controller = loader.getController();
            controller.setBookService(bookService);

            stage.show();
            populateBooksTable();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the add book form.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditBook() {
        Book selectedBook = booksTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Load edit book form
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/book/edit_book_form.fxml"));
                Parent root = loader.load();

                EditBookController controller = loader.getController();
                controller.setBookService(bookService);
                controller.setBook(selectedBook);

                Stage stage = new Stage();
                stage.setTitle("Edit Book");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not open the edit book form.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to edit.");
        }
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = booksTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Book");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected book?");
            confirmationAlert.setContentText("This action cannot be undone.");

            if (confirmationAlert.showAndWait().get() == ButtonType.OK) {
                bookService.deleteBook(selectedBook.getIsbn());
                populateBooksTable();
                showAlert(Alert.AlertType.INFORMATION, "Book Deleted", "Book successfully deleted!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
