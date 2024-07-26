package org.example.lms.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.lms.repository.BookRepository;
import org.example.lms.repository.BorrowedBookRepository;
import org.example.lms.service.AuthenticationService;
import org.example.lms.model.Librarian;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import org.example.lms.service.BookService;
import org.example.lms.service.BorrowedBookService;
import org.example.lms.util.DatabaseUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;

public class LoginController {

    @FXML
    private TextField emailField;  // TextField for entering email

    @FXML
    private PasswordField passwordField;  // PasswordField for entering password

    private AuthenticationService authService;  // Service for authenticating users

    // Setter for dependency injection
    public void setAuthenticationService(AuthenticationService authService) {
        this.authService = authService;
    }

    @FXML
    private void handleLogin() {
        // Get the email and password entered by the user
        String email = emailField.getText();
        String password = passwordField.getText();

        // Attempt to authenticate the librarian with the provided credentials
        Librarian librarian = authService.authenticateLibrarian(email, password);
        if (librarian != null) {
            navigateToDashboard("/org/example/lms/librarian_dashboard.fxml", "Librarian Dashboard");
            return;
        }
        // If authentication fails, show an error alert
        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
    }

    // Method to navigate to the dashboard
    private void navigateToDashboard(String fxmlFile, String title) {
        try {
            // Load the FXML file for the dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the controller from the loaded FXML
            Object controller = loader.getController();

            // If the controller is an instance of LibrarianDashboardController, set its services
            if (controller instanceof LibrarianDashboardController librarianController) {
                // Create the necessary repository instance
                Connection connection = DatabaseUtil.getInstance().getConnection();
                BorrowedBookRepository borrowedBookRepository = new BorrowedBookRepository(connection);

                // Initialize the service with the repository
                BorrowedBookService borrowedBookService = new BorrowedBookService(borrowedBookRepository);

                // Set the services on the controller
                BookRepository bookRepository = new BookRepository();
                librarianController.setBookService(new BookService(bookRepository)); // Provide the actual BookService instance
                librarianController.setBorrowedBookService(borrowedBookService);
            }

            // Set the scene and title for the stage (window) and show it
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException | SQLException e) {
            // If navigation fails, show an error alert and print the stack trace
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the dashboard.");
            e.printStackTrace();
        }
    }

    // Method to show an alert with a given type, title, and message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
