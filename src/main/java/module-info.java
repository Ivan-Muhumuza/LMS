module org.example.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.lms to javafx.fxml;
    exports org.example.lms;

    opens views to javafx.fxml;


    exports views;
}