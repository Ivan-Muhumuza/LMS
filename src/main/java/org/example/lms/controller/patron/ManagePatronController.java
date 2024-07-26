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
import org.example.lms.model.Patron;
import org.example.lms.service.BookService;
import org.example.lms.service.PatronService;
import org.example.lms.repository.PatronRepository;
import java.io.IOException;

public class ManagePatronController {
    // FXML fields for buttons and table views in the UI
    @FXML private TableView<Patron> patronsTableView;
    @FXML private TableColumn<Patron, Long> patronIDColumn;
    @FXML private TableColumn<Patron, String> nameColumn;
    @FXML private TableColumn<Patron, String> emailColumn;
    @FXML private TextField searchPatronField;
    @FXML private Button backToDashboardButton;

    // Dependencies for repositories and services
    private PatronRepository patronRepository;
    private PatronService patronService;
    private BookService bookService;

    // Constructor to initialize the PatronRepository and PatronService
    public ManagePatronController() {
        this.patronRepository = new PatronRepository();
        this.patronService = new PatronService(patronRepository);
    }

    // Method to initialize the TableView columns and load data
    @FXML
    private void initialize() {
        // Initialize TableView columns and load data
        patronIDColumn.setCellValueFactory(new PropertyValueFactory<>("patronID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Load patrons into the TableView
        populatePatronsTable(); // Call method to populate the TableView with patron data
    }

    // Method to populate the TableView with patrons
    private void populatePatronsTable() {
        if (patronsTableView != null) {
            // Fetch and set items to the TableView
            patronsTableView.setItems(FXCollections.observableArrayList(patronService.getAllPatrons()));  // Get all patrons and set to TableView
        } else {
            System.err.println("patronsTableView is null");  // Log an error if TableView is null
        }
    }

    // Method to handle adding a new patron
    @FXML
    private void handleAddPatron(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/patron/add_patron_form.fxml")); // Load the add patron form
            Parent root = loader.load(); // Load the form into a Parent object

            AddPatronController controller = loader.getController(); // Get the controller for the add patron form
            controller.setPatronService(patronService); // Set the PatronService in the add patron controller

            Stage stage = new Stage(); // Create a new stage for the add patron form
            stage.setTitle("Add Patron");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Set the modality of the stage
            stage.showAndWait(); // Show the stage and wait for it to close

            // Refresh the TableView to show the newly added patron
            populatePatronsTable();
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace if an IOException occurs
        }
    }


    // Method to handle editing a selected patron
    @FXML
    private void handleEditPatron() {
        Patron selectedPatron = patronsTableView.getSelectionModel().getSelectedItem(); // Get the selected patron from the TableView
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
                showAlert(Alert.AlertType.ERROR, "Error", "Could not open the edit patron form.");  // Show error alert if form cannot be opened
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a Patron to edit."); // Show warning alert if no patron is selected
        }
    }

    // Method to handle deleting a selected patron
    @FXML
    private void handleDeletePatron() {
        Patron selectedPatron = patronsTableView.getSelectionModel().getSelectedItem(); // Get the selected patron from the TableView
        if (selectedPatron != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION); // Create a confirmation alert
            confirmationAlert.setTitle("Delete Patron");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected Patron?");
            confirmationAlert.setContentText("This action cannot be undone.");

            if (confirmationAlert.showAndWait().get() == ButtonType.OK) { // Show the alert and wait for the user's response
                patronService.deletePatron(selectedPatron.getPatronID()); // Delete the selected patron
                populatePatronsTable(); // Refresh the TableView to show the remaining patrons
                showAlert(Alert.AlertType.INFORMATION, "Patron Deleted", "Patron successfully deleted!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a patron to delete.");
        }
    }


    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/lms/librarian/librarian_dashboard.fxml"));
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
