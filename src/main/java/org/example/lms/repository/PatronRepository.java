package org.example.lms.repository;

import org.example.lms.model.Patron;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronRepository {

    private Connection connection;

    public PatronRepository() throws SQLException {
        this.connection = DatabaseUtil.getInstance().getConnection();
    }

    public void addPatronToDatabase(Patron patron) throws SQLException {
        String query = "INSERT INTO Patron (PatronID, Name, Email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patron.getPatronID());
            statement.setString(2, patron.getName());
            statement.setString(3, patron.getEmail());
            statement.executeUpdate();
        }
    }

    public void updatePatronInDatabase(Patron patron) throws SQLException {
        String query = "UPDATE Patron SET Name = ?, Email = ? WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.setInt(3, patron.getPatronID());
            statement.executeUpdate();
        }
    }

    public void deletePatronFromDatabase(int patronID) throws SQLException {
        String query = "DELETE FROM Patron WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronID);
            statement.executeUpdate();
        }
    }

    public Patron getPatronFromDatabase(int patronID) throws SQLException {
        String query = "SELECT * FROM Patron WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Patron(
                        resultSet.getInt("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password")
                );
            }
            return null;
        }
    }

    public Patron getPatronByEmail(String email) {
        String query = "SELECT * FROM Patron WHERE Email = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Patron(
                        resultSet.getInt("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return null;
    }


}
