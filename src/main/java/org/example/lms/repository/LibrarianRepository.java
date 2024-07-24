package org.example.lms.repository;

import org.example.lms.model.Librarian;

import org.example.lms.util.DatabaseUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibrarianRepository {

    public Librarian getLibrarianByEmail(String email) {
        String query = "SELECT * FROM Librarian WHERE Email = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Librarian(
                        resultSet.getInt("LibrarianID"),
                        resultSet.getInt("LibraryID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addLibrarianToDatabase(Librarian librarian) {
        String query = "INSERT INTO Librarian (LibrarianID, LibraryID, Name, Email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setLibrarianParameters(statement, librarian);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLibrarianInDatabase(Librarian librarian) {
        String query = "UPDATE Librarian SET LibraryID = ?, Name = ?, Email = ? WHERE LibrarianID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, librarian.getLibraryID());
                statement.setString(2, librarian.getName());
                statement.setString(3, librarian.getEmail());
                statement.setLong(4, librarian.getLibrarianID());
                statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLibrarianFromDatabase(int librarianID) {
        String query = "DELETE FROM Librarian WHERE LibrarianID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, librarianID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Librarian getLibrarianFromDatabase(int librarianID) {
        String query = "SELECT * FROM Librarian WHERE LibrarianID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, librarianID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Librarian(
                        resultSet.getInt("LibrarianID"),
                        resultSet.getInt("LibraryID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setLibrarianParameters(PreparedStatement statement, Librarian librarian) throws SQLException {
        statement.setInt(1, librarian.getLibrarianID());
        statement.setInt(2, librarian.getLibraryID());
        statement.setString(3, librarian.getName());
        statement.setString(4, librarian.getEmail());
    }
}
