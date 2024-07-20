package org.example.lms.controller;

import org.example.lms.service.AuthenticationService;
import org.example.lms.model.Librarian;
import org.example.lms.model.Patron;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private AuthenticationService authService;

    // Setter for dependency injection
    public void setAuthenticationService(AuthenticationService authService) {
        this.authService = authService;
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        Librarian librarian = authService.authenticateLibrarian(email, password);
        if (librarian != null) {
            // Navigate to librarian dashboard
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, Librarian " + librarian.getName());
            return;
        }

        Patron patron = authService.authenticatePatron(email, password);
        if (patron != null) {
            // Navigate to patron dashboard
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, Patron " + patron.getName());
            return;
        }

        showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password.");
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}