package org.example.lms.app;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.example.lms.service.AuthenticationService;
import org.example.lms.controller.LoginController;
import org.example.lms.repository.LibrarianRepository;
import org.example.lms.repository.PatronRepository;

import java.io.IOException;
import java.sql.SQLException;

public class LMSApplicationTest extends ApplicationTest {

    private LMSApplication app;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        app = new LMSApplication();
        app.start(stage);
    }

    @Test
    public void testFXMLLoading() {
        assertDoesNotThrow(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(LMSApplication.class.getResource("/org/example/lms/app/login.fxml"));
            fxmlLoader.load();
        });
    }

    @Test
    public void testRepositoryCreation() {
        LibrarianRepository librarianRepository = new LibrarianRepository();
        PatronRepository patronRepository = new PatronRepository();
        assertNotNull(librarianRepository);
        assertNotNull(patronRepository);
    }

    @Test
    public void testAuthenticationServiceSetup() throws SQLException {
        LibrarianRepository librarianRepository = mock(LibrarianRepository.class);
        PatronRepository patronRepository = mock(PatronRepository.class);
        AuthenticationService authService = new AuthenticationService(librarianRepository, patronRepository);
        assertNotNull(authService);
    }

    @Test
    public void testLoginControllerInitialization() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(LMSApplication.class.getResource("/org/example/lms/app/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        LibrarianRepository librarianRepository = new LibrarianRepository();
        PatronRepository patronRepository = new PatronRepository();
        AuthenticationService authService = new AuthenticationService(librarianRepository, patronRepository);

        LoginController loginController = fxmlLoader.getController();
        loginController.setAuthenticationService(authService);

        assertNotNull(loginController);
        assertEquals(authService, loginController.getAuthenticationService());
    }

    @Test
    public void testStageConfiguration() {
        assertEquals("Library Management System", stage.getTitle());
        assertNotNull(stage.getScene());
        assertEquals(320, stage.getScene().getWidth());
        assertEquals(240, stage.getScene().getHeight());
    }
}
