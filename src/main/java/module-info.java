module org.example.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.dbcp2;
    requires jbcrypt;
    requires mysql.connector.j;
    requires com.h2database;


    exports org.example.lms;
    exports org.example.lms.model;
    exports org.example.lms.util;
    exports org.example.lms.controller.book;
    exports org.example.lms.controller;
    exports org.example.lms.controller.patron;
    exports org.example.lms.controller.transaction;

    opens org.example.lms.controller to javafx.fxml;
    opens org.example.lms.model to javafx.fxml;
    opens org.example.lms.controller.book to javafx.fxml;
    opens org.example.lms.util to javafx.fxml;
    opens org.example.lms.controller.patron to javafx.fxml;
    opens org.example.lms.controller.transaction to javafx.fxml;
    opens org.example.lms.repository to org.mockito;
    opens org.example.lms.service to org.mockito;
}