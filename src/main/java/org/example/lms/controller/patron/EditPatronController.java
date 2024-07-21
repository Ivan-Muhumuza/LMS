package org.example.lms.controller.patron;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.lms.model.Book;
import org.example.lms.model.Patron;
import org.example.lms.service.BookService;
import org.example.lms.service.PatronService;

import java.sql.SQLException;

public class EditPatronController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField patronIDField;

    private Patron patron;

    private PatronService patronService;

    public EditPatronController(){
        this.patronService = new PatronService();
    }

    public void setPatronService(PatronService patronService) {
        this.patronService = patronService;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
        populatePatronDetails();
    }

    private void populatePatronDetails() {
        if (patron != null) {
            patronIDField.setText(String.valueOf(patron.getPatronID()));
            nameField.setText(patron.getName());
            emailField.setText(patron.getEmail());
        }
    }

    @FXML
    private void handleEditPatron() {
        if (patron != null) {
            try {
                patron.setName(nameField.getText());
                patron.setEmail(emailField.getText());

                patronService.updatePatron(patron);
                showAlert(Alert.AlertType.INFORMATION, "Patron Updated", "Patron successfully updated!");
                closeWindow();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Patron information is not available.");
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
