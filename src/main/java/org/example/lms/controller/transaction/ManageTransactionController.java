package org.example.lms.controller.transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.lms.model.Patron;
import org.example.lms.repository.PatronRepository;
import org.example.lms.service.BorrowedBookService;
import org.example.lms.util.DatabaseUtil;
import org.example.lms.controller.book.ManageBookController;

public class ManageTransactionController {

    @FXML
    private ComboBox<Long> patronIDComboBox;
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
    private Button returnSelectedBookButton;

    @FXML
    private Button viewBorrowedBooksButton;
    @FXML
    private Button backToDashboardButton;
    @FXML
    private BorrowedBookService borrowedBookService;
    private PatronRepository patronRepository;

    @FXML
    private void initialize() throws SQLException {
        // Initialize with a database connection
        Connection connection = DatabaseUtil.getInstance().getConnection();
        this.borrowedBookService = new BorrowedBookService(connection);

        this.patronRepository = new PatronRepository();
        setupPatronIDComboBox();
    }

    public void setBorrowedBookService(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    private void setupPatronIDComboBox() throws SQLException {
        List<Patron> patrons = patronRepository.getAllPatrons();
        if (patrons == null || patrons.isEmpty()) {
            patronIDComboBox.setItems(FXCollections.observableArrayList());
            return;
        }

        // Convert patron list to IDs
        List<Long> patronIds = patrons.stream().map(Patron::getPatronID).collect(Collectors.toList());

        // Set up the ComboBox with Long values
        ObservableList<Long> observablePatronIds = FXCollections.observableArrayList(patronIds);
        patronIDComboBox.setItems(observablePatronIds);
    }



    @FXML
    private void handleBorrowBook() {
        try {
            // Retrieve and cast the value safely
            Object selectedValue = patronIDComboBox.getValue();
            if (!(selectedValue instanceof Long)) {
                throw new IllegalArgumentException("Selected Patron ID is not a Long");
            }
            Long patronID = (Long) selectedValue;

            // Proceed with other fields and service call
            String isbn = isbnField.getText();
            LocalDateTime borrowedDate = LocalDateTime.now();
            LocalDate dueDate = dueDatePicker.getValue();
            LocalDateTime dueDateTime = dueDate.atStartOfDay();

            borrowedBookService.borrowBook(patronID, isbn, borrowedDate, dueDateTime);
            showAlert(Alert.AlertType.INFORMATION, "Book Borrowed", "Book successfully borrowed!");
        } catch (SQLException | IllegalArgumentException e) {
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


//    @FXML
//    private void handleViewBorrowedBooks(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/transaction/borrowed_books_view.fxml"));
//            Parent root = loader.load();
//
//            BorrowedBooksViewController controller = loader.getController();
//            controller.setBorrowedBookService(borrowedBookService);
//
//            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
//            currentStage.setScene(scene);
//            currentStage.setTitle("Borrowed Books");
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the borrowed books view.");
//            e.printStackTrace();
//        }
//    }


    @FXML
    private void handleViewBorrowedBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/transaction/borrowed_books_view.fxml"));
            Parent root = loader.load();

            BorrowedBooksViewController controller = loader.getController();
            controller.setBorrowedBookService(borrowedBookService);

            Stage newStage = new Stage();
            newStage.setTitle("Borrowed Books");
            newStage.setScene(new Scene(root));
            newStage.initModality(Modality.APPLICATION_MODAL); // Optional: makes the new stage modal
            newStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the borrowed books view.");
            e.printStackTrace();
        }
    }




    @FXML
    private void handleBackToDashboard() throws IOException {
        // Load the Dashboard view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/librarian_dashboard.fxml"));
        ManageBookController.populateBooksTable();
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) backToDashboardButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
