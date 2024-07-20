package org.example.lms.repository;

import org.example.lms.model.Patron;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatronRepository {

    public void addPatronToDatabase(Patron patron) throws SQLException {
        String query = "INSERT INTO Patron (PatronID, Name, Email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, patron.getPatronID());
            statement.setString(2, patron.getName());
            statement.setString(3, patron.getEmail());
            statement.executeUpdate();
        }
    }


    public void updatePatronInDatabase(Patron patron) {
        String query = "UPDATE Patron SET Name = ?, Email = ? WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.setInt(3, patron.getPatronID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatronFromDatabase(int patronID) {
        String query = "DELETE FROM Patron WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Patron getPatronFromDatabase(int patronID) {
        String query = "SELECT * FROM Patron WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patronID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Patron(
                        resultSet.getInt("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                        resultSet.getString("Email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

