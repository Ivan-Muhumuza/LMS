package org.example.lms.controller.patron;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.lms.controller.LibrarianDashboardController;
import org.example.lms.controller.book.EditBookController;
import org.example.lms.model.Book;
import org.example.lms.model.Patron;
import org.example.lms.service.BookService;
import org.example.lms.service.PatronService;
import org.example.lms.repository.PatronRepository;
import java.io.IOException;
import java.sql.SQLException;

public class ManagePatronController {
    @FXML private TableView<Patron> patronsTableView;
    @FXML private TableColumn<Patron, Long> patronIDColumn;
    @FXML private TableColumn<Patron, String> nameColumn;
    @FXML private TableColumn<Patron, String> emailColumn;
    private BookService bookService;
    @FXML private TextField searchPatronField;
    @FXML private Button backToDashboardButton;

    private PatronRepository patronRepository;
    private PatronService patronService = new PatronService(patronRepository);

    public ManagePatronController(){
    }


    @FXML
    private void initialize() {
        // Initialize TableView columns and load data
        patronIDColumn.setCellValueFactory(new PropertyValueFactory<>("patronID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load patrons into the TableView
        populatePatronsTable();
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
    private void handleAddPatron(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/patron/add_patron_form.fxml"));
            Parent root = loader.load();

            AddPatronController controller = loader.getController();
            controller.setPatronService(patronService);

            Stage stage = new Stage();
            stage.setTitle("Add Patron");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh the patrons table or perform other necessary actions
            populatePatronsTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleEditPatron() {
        Patron selectedPatron = patronsTableView.getSelectionModel().getSelectedItem();
        if (selectedPatron != null) {
            // Load edit book form
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/patron/edit_patron_form.fxml"));
                Parent root = loader.load();

                EditPatronController controller = loader.getController();
                controller.setPatronService(patronService);
                controller.setPatron(selectedPatron);

                Stage stage = new Stage();
                stage.setTitle("Edit Patron");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not open the edit patron form.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a Patron to edit.");
        }
    }

    @FXML
    private void handleDeletePatron() {
        Patron selectedPatron = patronsTableView.getSelectionModel().getSelectedItem();
        if (selectedPatron != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Patron");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected Patron?");
            confirmationAlert.setContentText("This action cannot be undone.");

            if (confirmationAlert.showAndWait().get() == ButtonType.OK) {
                patronService.deletePatron(selectedPatron.getPatronID());
                populatePatronsTable();
                showAlert(Alert.AlertType.INFORMATION, "Patron Deleted", "Patron successfully deleted!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a patron to delete.");
        }
    }


    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/librarian_dashboard.fxml"));
            Parent root = loader.load();

            LibrarianDashboardController controller = loader.getController();
            controller.setPatronService(patronService); // Ensure PatronService is set again

            Stage stage = (Stage) backToDashboardButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Dashboard");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the dashboard scene.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        // Implement search functionality
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
