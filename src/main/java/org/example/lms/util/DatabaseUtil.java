package org.example.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.lms.model.Librarian;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.*;

public class DatabaseUtil {
    private static DatabaseUtil instance;
    private DataSource dataSource;

    private DatabaseUtil() {
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL("jdbc:mysql://localhost:3306/library_schema");
        mysqlDS.setUser("root");
        mysqlDS.setPassword("Pioneer4!");
        this.dataSource = mysqlDS;
    }

    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Method to hash passwords
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method to check passwords
    public static boolean checkPassword(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    // Method to authenticate user
    public Object authenticateUser(String email, String password) throws SQLException {
        try (Connection connection = getConnection()) {
            String librarianQuery = "SELECT * FROM Librarian WHERE Email = ?";

            try (PreparedStatement librarianStatement = connection.prepareStatement(librarianQuery)) {
                librarianStatement.setString(1, email);
                try (ResultSet librarianResultSet = librarianStatement.executeQuery()) {
                    if (librarianResultSet.next()) {
                        String storedPassword = librarianResultSet.getString("Password");
                        if (checkPassword(password, storedPassword)) {
                            return new Librarian(
                                    librarianResultSet.getInt("LibrarianID"),
                                    librarianResultSet.getInt("LibraryID"),
                                    librarianResultSet.getString("Name"),
                                    librarianResultSet.getString("Email"),
                                    librarianResultSet.getString("Password")
                            );
                        }
                    }
                }
            }

        }
        return null; // User not found or password incorrect
    }
}