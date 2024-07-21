package org.example.lms.controller.transaction;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.example.lms.model.BorrowedBook;
import org.example.lms.service.BorrowedBookService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class BorrowedBooksViewController {

    @FXML
    private ComboBox<BorrowedBook> borrowedBookComboBox;

    @FXML
    private Button returnSelectedBookButton;

    private BorrowedBookService borrowedBookService;
    private ObservableList<BorrowedBook> borrowedBooksList = FXCollections.observableArrayList();


    @FXML
    private TableView<BorrowedBook> borrowedBooksTableView;

    @FXML
    private TableColumn<BorrowedBook, String> isbnColumn;

    @FXML
    private TableColumn<BorrowedBook, Long> patronIDColumn;

    @FXML
    private TableColumn<BorrowedBook, LocalDateTime> borrowedDateColumn;

    @FXML
    private TableColumn<BorrowedBook, LocalDateTime> dueDateColumn;


    public void setBorrowedBookService(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    @FXML
    public void initialize() {
        isbnColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getIsbn()));
        patronIDColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPatronID()));
        borrowedDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBorrowedDate()));
        dueDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDueDate()));

        borrowedBooksTableView.setItems(borrowedBooksList);
        borrowedBookComboBox.setItems(borrowedBooksList);

        if (borrowedBookService != null) {
            loadBorrowedBooks();
        } else {
            System.out.println("borrowedBookService is not set.");
        }
    }


    private void loadBorrowedBooks() {
        try {
            List<BorrowedBook> borrowedBooks = borrowedBookService.getAllBorrowedBooks();
            borrowedBooksList.setAll(borrowedBooks);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Unable to load borrowed books: " + e.getMessage());
        }
    }

    @FXML
    private void handleReturnSelectedBook() {
        BorrowedBook selectedBook = borrowedBookComboBox.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                borrowedBookService.returnBook(selectedBook.getPatronID(), selectedBook.getIsbn());
                showAlert(AlertType.INFORMATION, "Book Returned", "Book successfully returned!");
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Error", e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "No Selection", "Please select a book to return.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}




