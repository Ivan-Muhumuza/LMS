package org.example.lms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.lms.controller.LoginController;
import org.example.lms.repository.LibrarianRepository;
import org.example.lms.repository.PatronRepository;
import org.example.lms.service.AuthenticationService;

import java.io.IOException;
import java.sql.SQLException;

public class LMSApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(LMSApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        // Create repositories
        LibrarianRepository librarianRepository = new LibrarianRepository();
        PatronRepository patronRepository = new PatronRepository();

        // Set up AuthenticationService with repositories
        AuthenticationService authService = new AuthenticationService(librarianRepository, patronRepository);

        // Access LoginController and set AuthenticationService
        LoginController loginController = fxmlLoader.getController();
        loginController.setAuthenticationService(authService);

        // Configure stage
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
