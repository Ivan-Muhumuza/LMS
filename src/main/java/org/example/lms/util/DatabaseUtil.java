package org.example.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.example.lms.model.Librarian;
import org.example.lms.model.Patron;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DatabaseUtil {
    private static DatabaseUtil instance;
    private Connection connection;

    private DatabaseUtil() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/library_schema";
        String user = "root";
        String password = "Password123";
        connection = DriverManager.getConnection(url, user, password);
    }

    public static synchronized DatabaseUtil getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
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
        String librarianQuery = "SELECT * FROM Librarian WHERE Email = ?";
        String patronQuery = "SELECT * FROM Patron WHERE Email = ?";

        PreparedStatement librarianStatement = connection.prepareStatement(librarianQuery);
        librarianStatement.setString(1, email);
        ResultSet librarianResultSet = librarianStatement.executeQuery();

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

        PreparedStatement patronStatement = connection.prepareStatement(patronQuery);
        patronStatement.setString(1, email);
        ResultSet patronResultSet = patronStatement.executeQuery();

        if (patronResultSet.next()) {
            String storedPassword = patronResultSet.getString("Password");
            if (checkPassword(password, storedPassword)) {
                return new Patron(
                        patronResultSet.getInt("PatronID"),
                        patronResultSet.getString("Name"),
                        patronResultSet.getString("Email"),
                        patronResultSet.getString("Password")
                );
            }
        }

        return null; // User not found or password incorrect
    }
}
