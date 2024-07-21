//package org.example.lms.controller.transaction;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.collections.transformation.FilteredList;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.TextField;
//import org.example.lms.model.Patron;
//import org.example.lms.repository.BorrowedBookRepository;
//import org.example.lms.repository.PatronRepository;
//import org.example.lms.service.BorrowedBookService;
//import org.example.lms.util.DatabaseUtil;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class BorrowBookController {
////    @FXML
////    private TextField patronIDField;
//    @FXML
//    private ComboBox<Long> patronIDComboBox;
//    @FXML
//    private TextField searchPatronField;
//    @FXML
//    private TextField isbnField;
//    @FXML
//    private DatePicker dueDatePicker;
//
//    private BorrowedBookService borrowedBookService;
//    private PatronRepository patronRepository;
//
//    @FXML
//    private void initialize() throws SQLException {
//        // Initialize with a database connection
//        Connection connection = DatabaseUtil.getInstance().getConnection();
//        this.borrowedBookService = new BorrowedBookService(connection);
//
//        this.patronRepository = new PatronRepository();
//        setupPatronIDComboBox();
//    }
//
//    private void setupPatronIDComboBox() throws SQLException {
//        List<Patron> patrons = patronRepository.getAllPatrons();
//        System.out.println("Fetched patrons: " + patrons); // Debug statement
//
//        if (patrons == null || patrons.isEmpty()) {
//            patronIDComboBox.setItems(FXCollections.observableArrayList());
//            return;
//        }
//
//        List<Long> patronIds = patrons.stream().map(Patron::getPatronID).collect(Collectors.toList());
//        System.out.println("Patron IDs: " + patronIds); // Debug statement
//
//        ObservableList<Long> observablePatronIds = FXCollections.observableArrayList(patronIds);
//        patronIDComboBox.setItems(observablePatronIds);
//        patronIDComboBox.setEditable(true);
//
//        patronIDComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
//            if (newValue == null || newValue.isEmpty()) {
//                patronIDComboBox.setItems(observablePatronIds);
//            } else {
//                ObservableList<Long> filteredIds = observablePatronIds.stream()
//                        .filter(id -> id.toString().contains(newValue))
//                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
//                patronIDComboBox.setItems(filteredIds);
//            }
//        });
//    }
//
//    private void setupPatronIDComboBox() throws SQLException {
//        // Fetch patrons from repository
//        List<Patron> patrons = patronRepository.getAllPatrons();
//        System.out.println("Fetched patrons: " + patrons); // Debug statement
//
//        if (patrons == null || patrons.isEmpty()) {
//            patronIDComboBox.setItems(FXCollections.observableArrayList());
//            return;
//        }
//
//        // Extract patron IDs
//        List<Long> patronIds = patrons.stream().map(Patron::getPatronID).collect(Collectors.toList());
//        System.out.println("Patron IDs: " + patronIds); // Debug statement
//
//        ObservableList<Long> observablePatronIds = FXCollections.observableArrayList(patronIds);
//        patronIDComboBox.setItems(observablePatronIds);
//
//        // Set up listener for search text field
//        searchPatronField.textProperty().addListener((obs, oldValue, newValue) -> {
//            filterPatronIDs(newValue);
//        });
//
//        // Allow ComboBox to handle user input
//        patronIDComboBox.setEditable(true);
//        patronIDComboBox.getEditor().setPromptText("Type to search...");
//    }
//    private void filterPatronIDs(String filter) {
//        // Fetch patrons from repository
//        List<Patron> patrons = patronRepository.getAllPatrons();
//        List<Long> filteredIds = patrons.stream()
//                .map(Patron::getPatronID)
//                .filter(id -> id.toString().contains(filter))
//                .collect(Collectors.toList());
//
//        patronIDComboBox.setItems(FXCollections.observableArrayList(filteredIds));
//    }
//
//
//
//    @FXML
//    private void handleBorrowBook() {
//        try {
////            long patronID = Long.parseLong(patronIDField.getText());
//            Long patronID = patronIDComboBox.getValue();
//            String isbn = isbnField.getText();
//            LocalDateTime borrowedDate = LocalDateTime.now();
//            LocalDate dueDate = dueDatePicker.getValue();
//            LocalDateTime dueDateTime = dueDate.atStartOfDay();
//
//            borrowedBookService.borrowBook(patronID, isbn, borrowedDate, dueDateTime);
//            showAlert(Alert.AlertType.INFORMATION, "Book Borrowed", "Book successfully borrowed!");
//        } catch (SQLException | NumberFormatException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
//        }
//    }
//
//    @FXML
//    private void handleReturnBook() {
//        try {
////            long patronID = Long.parseLong(patronIDField.getText());
//            Long patronID = patronIDComboBox.getValue();
//            String isbn = isbnField.getText();
//
//            borrowedBookService.returnBook(patronID, isbn);
//            showAlert(Alert.AlertType.INFORMATION, "Book Returned", "Book successfully returned!");
//        } catch (SQLException | NumberFormatException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
//        }
//    }
//
//    private void showAlert(Alert.AlertType type, String title, String message) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}