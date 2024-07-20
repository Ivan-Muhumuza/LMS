package org.example.lms.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.lms.repository.TransactionRepository;
import org.example.lms.service.AuthenticationService;
import org.example.lms.model.Librarian;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.example.lms.service.BookService;
import org.example.lms.service.TransactionService;

import java.io.IOException;

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
            navigateToDashboard("/org/example/lms/librarian_dashboard.fxml", "Librarian Dashboard");
            return;
        }
        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
    }

    // Added method
    private void navigateToDashboard(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the controller from the loaded FXML
            Object controller = loader.getController();

            if (controller instanceof LibrarianDashboardController librarianController) {
                // Create the necessary repository instance
                TransactionRepository transactionRepository = new TransactionRepository();

                // Initialize the service with the repository
                TransactionService transactionService = new TransactionService(transactionRepository);

                // Set the services on the controller
                librarianController.setBookService(new BookService()); // Provide the actual BookService instance
                librarianController.setTransactionService(transactionService);
            }

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the dashboard.");
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
