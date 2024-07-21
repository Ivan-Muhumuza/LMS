package org.example.lms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.lms.controller.book.AddBookController;
import org.example.lms.controller.book.ManageBookController;
import org.example.lms.controller.transaction.ManageTransactionController;
import org.example.lms.model.Book;
import org.example.lms.model.Librarian;
import org.example.lms.model.Patron;
import org.example.lms.service.BookService;
import org.example.lms.service.BorrowedBookService;
import org.example.lms.service.PatronService;
import org.example.lms.service.TransactionService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class LibrarianDashboardController {

    @FXML
    private Button manageTransactionsButton;
    @FXML
    private Button managePatronsButton;
    @FXML
    private Button manageBooksButton;
    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableView<Patron> patronsTableView;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TextField searchField;

    private BookService bookService;

    private PatronService patronService;

    private TransactionService transactionService;

    private BorrowedBookService borrowedBookService;

    // Setters for dependency injection
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
        populateBooksTable();
    }

    public void setPatronService(PatronService patronService) {
        this.patronService = patronService;
        populatePatronsTable();
    }

    public void setBorrowedBookService(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    @FXML
    private void initialize() {
        // Initialize table columns
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Set button actions
        manageTransactionsButton.setOnAction(e -> handleManageTransactions());
        managePatronsButton.setOnAction(e -> handleManagePatrons());
        manageBooksButton.setOnAction(e -> handleManageBooks());

        // Add search field listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));

        // Populate books table when the controller is initialized
        populateBooksTable();
    }

    private void populateBooksTable() {
        if (bookService != null) {
            ObservableList<Book> books = bookService.getAllBooks();
            booksTableView.setItems(books);
        }
    }

    private void populatePatronsTable() {
        if (patronsTableView != null) {
            // Fetch and set items to the TableView
            patronsTableView.setItems(FXCollections.observableArrayList(patronService.getAllPatrons()));
        } else {
            System.err.println("patronsTableView is null");
        }
    }

    @FXML
    private void handleManagePatrons() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/patron/manage_patron.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) managePatronsButton.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Manage Patrons");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the manage patrons page.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleManageBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/book/manage_book.fxml"));
            Parent root = loader.load();

            ManageBookController controller = loader.getController();
            controller.setBookService(bookService);

            // Switch to the manage books scene
            Stage stage = (Stage) manageBooksButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Books");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the manage book scene.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleManageTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/transaction/manage_transaction.fxml"));
            Parent root = loader.load();

            ManageTransactionController controller = loader.getController();
            controller.setBorrowedBookService(borrowedBookService);

            // Switch to the manage transactions scene
            Stage stage = (Stage) manageTransactionsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Transactions");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the manage transactions scene.");
            e.printStackTrace();
        }
    }

    private void handleSearch(String query) {
        if (bookService != null) {
            List<Book> filteredBooks = bookService.searchBooks(query);
            booksTableView.setItems(FXCollections.observableArrayList(filteredBooks));
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
