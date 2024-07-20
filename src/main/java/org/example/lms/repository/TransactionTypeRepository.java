package org.example.lms.repository;

import org.example.lms.model.TransactionType;
import org.example.lms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionTypeRepository {

    public void addTransactionTypeToDatabase(TransactionType transactionType) throws SQLException {
        String query = "INSERT INTO TransactionType (TypeDescription) VALUES (?)";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, transactionType.getTypeDescription());
            statement.executeUpdate();
        }
    }

    public void updateTransactionTypeInDatabase(TransactionType transactionType) throws SQLException {
        String query = "UPDATE TransactionType SET TypeDescription = ? WHERE TypeID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, transactionType.getTypeDescription());
            statement.setInt(2, transactionType.getTypeID());
            statement.executeUpdate();
        }
    }

    public void deleteTransactionTypeFromDatabase(int typeID) throws SQLException {
        String query = "DELETE FROM TransactionType WHERE TypeID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, typeID);
            statement.executeUpdate();
        }
    }

    public TransactionType getTransactionTypeFromDatabase(int typeID) throws SQLException {
        String query = "SELECT * FROM TransactionType WHERE TypeID = ?";
        try (Connection connection = DatabaseUtil.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, typeID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionType(resultSet.getInt("TypeID"), resultSet.getString("TypeDescription"));
            }
            return null;
        }
    }
}

