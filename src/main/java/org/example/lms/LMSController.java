package org.example.lms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LMSController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}