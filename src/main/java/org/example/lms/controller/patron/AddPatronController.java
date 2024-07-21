package org.example.lms.controller.patron;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.lms.model.Patron;
import org.example.lms.service.PatronService;
import org.example.lms.util.PatronIDGenerator;

public class AddPatronController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;

    private PatronService patronService = new PatronService();

    public void setPatronService(PatronService patronService) {
        this.patronService = patronService;
    }

    public AddPatronController() {
    }

    @FXML
    private void handleAddPatron(ActionEvent event) {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            long patronID = PatronIDGenerator.generateUniqueID();

            Patron patron = new Patron(patronID, name, email);
            patronService.addPatron(patron);

            showAlert(Alert.AlertType.INFORMATION, "Patron Added", "Patron successfully added!");
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
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}
