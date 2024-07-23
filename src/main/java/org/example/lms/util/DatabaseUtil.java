package org.example.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.lms.model.Librarian;
import org.mindrot.jbcrypt.BCrypt;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.h2.jdbcx.JdbcDataSource;

//import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.*;

public class DatabaseUtil {
    private static DatabaseUtil instance;
    private DataSource dataSource;
    private static boolean useTestDatabase = false; // Add this flag to switch databases

    private DatabaseUtil() {
        if (useTestDatabase) {
            // Initialize H2 DataSource
            JdbcDataSource h2DataSource = new JdbcDataSource();
            h2DataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            h2DataSource.setUser("sa");
            h2DataSource.setPassword("");
            this.dataSource = h2DataSource;
        } else {
            // Initialize MySQL DataSource
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL("jdbc:mysql://localhost:3306/library_schema");
            mysqlDataSource.setUser("root");
            mysqlDataSource.setPassword("password123");
            this.dataSource = mysqlDataSource;
        }
    }

    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public static void setUseTestDatabase(boolean useTestDatabase) {
        // Method to change the database mode
        DatabaseUtil.useTestDatabase = useTestDatabase;
        // Reset instance to apply changes
        instance = null;
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