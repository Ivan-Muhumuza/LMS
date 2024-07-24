package org.example.lms.repository;

import org.example.lms.model.Book;
import org.example.lms.model.Patron;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatronRepository {


    public void addPatron(Patron patron) {
        String query = "INSERT INTO Patron (PatronID, Name, Email) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, patron.getPatronID());
            statement.setString(2, patron.getName());
            statement.setString(3, patron.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatron(Patron patron) {
        String query = "UPDATE Patron SET Name = ?, Email = ? WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.setLong(3, patron.getPatronID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatron(long patronID) {
        String query = "DELETE FROM Patron WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, patronID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Patron getPatron(Long patronID) {
        String query = "SELECT * FROM Patron WHERE PatronID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, patronID);
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

    public List<Patron> getAllPatrons() {
        List<Patron> patrons = new ArrayList<>();
        String query = "SELECT * FROM Patron";

        return getPatrons(patrons, query);
    }

    public Patron getPatronByEmail(String email) {
        String query = "SELECT * FROM Patron WHERE Email = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Patron(
                        resultSet.getLong("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Patron> getPatrons(List<Patron> patrons, String query) {
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Patron patron = new Patron(
                        resultSet.getLong("PatronID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email")
                );
                patrons.add(patron);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return patrons;
    }

}

