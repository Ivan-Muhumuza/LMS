module org.example.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.dbcp2;
    requires jbcrypt;

    opens org.example.lms.controller to javafx.fxml;
    exports org.example.lms;

    opens org.example.lms.model to javafx.fxml;


    exports org.example.lms.model;
    exports org.example.lms.util;
    opens org.example.lms.util to javafx.fxml;
}