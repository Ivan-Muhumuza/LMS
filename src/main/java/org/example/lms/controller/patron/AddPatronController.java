package org.example.lms.controller.patron;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.lms.model.Patron;
import org.example.lms.service.PatronService;
import org.example.lms.util.PatronIDGenerator;

import java.sql.SQLException;

public class AddPatronController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;

    private PatronService patronService;

    @FXML
    private void handleAddPatron() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();

            if (name.isEmpty() || email.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }

            // Generate a unique Patron ID
            Integer patronID = (int) PatronIDGenerator.generateUniqueID();

            // Create a new Patron instance
            Patron newPatron = new Patron(patronID, name, email);

            // Add the Patron using PatronService
            patronService.addPatron(newPatron);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Patron added successfully!");
            closeWindow();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database error occurred.");
            e.printStackTrace();
        }
    }

    public void setPatronService(PatronService patronService) {
        this.patronService = patronService;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        // Implement method to close the window
    }
}
