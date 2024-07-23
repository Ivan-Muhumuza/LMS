//package org.example.lms.service;
//
//import org.example.lms.model.TransactionType;
//import org.example.lms.repository.TransactionTypeRepository;
//
//import java.sql.SQLException;
//
//public class TransactionTypeService {
//
//    private final TransactionTypeRepository transactionTypeRepository;
//
//    public TransactionTypeService() {
//        this.transactionTypeRepository = new TransactionTypeRepository();
//    }
//
//    public void addTransactionType(TransactionType transactionType) throws SQLException {
//        // Validate input
//        if (transactionType == null || transactionType.getTypeDescription() == null || transactionType.getTypeDescription().isEmpty()) {
//            throw new IllegalArgumentException("Transaction Type description cannot be null or empty.");
//        }
//
//        // Check for existing type
//        TransactionType existingType = transactionTypeRepository.getTransactionTypeFromDatabase(transactionType.getTypeID());
//        if (existingType != null) {
//            throw new IllegalStateException("Transaction Type already exists.");
//        }
//
//        // Add to database
//        transactionTypeRepository.addTransactionTypeToDatabase(transactionType);
//    }
//
//    public void updateTransactionType(TransactionType transactionType) throws SQLException {
//        // Validate input
//        if (transactionType == null || transactionType.getTypeID() <= 0) {
//            throw new IllegalArgumentException("Invalid Transaction Type.");
//        }
//
//        // Check if the Transaction Type exists
//        TransactionType existingType = transactionTypeRepository.getTransactionTypeFromDatabase(transactionType.getTypeID());
//        if (existingType == null) {
//            throw new IllegalStateException("Transaction Type not found.");
//        }
//
//        // Validate the description
//        if (transactionType.getTypeDescription() == null || transactionType.getTypeDescription().isEmpty()) {
//            throw new IllegalArgumentException("Transaction Type description cannot be null or empty.");
//        }
//
//        // Update in database
//        transactionTypeRepository.updateTransactionTypeInDatabase(transactionType);
//    }
//
//    public void deleteTransactionType(int typeID) throws SQLException {
//        // Validate input
//        if (typeID <= 0) {
//            throw new IllegalArgumentException("Invalid Transaction Type ID.");
//        }
//
//        // Check if the Transaction Type exists
//        TransactionType existingType = transactionTypeRepository.getTransactionTypeFromDatabase(typeID);
//        if (existingType == null) {
//            throw new IllegalStateException("Transaction Type not found.");
//        }
//
//        // Delete from database
//        transactionTypeRepository.deleteTransactionTypeFromDatabase(typeID);
//    }
//
//    public TransactionType getTransactionType(int typeID) throws SQLException {
//        // Validate input
//        if (typeID <= 0) {
//            throw new IllegalArgumentException("Invalid Transaction Type ID.");
//        }
//
//        // Fetch from database
//        TransactionType transactionType = transactionTypeRepository.getTransactionTypeFromDatabase(typeID);
//        if (transactionType == null) {
//            throw new IllegalStateException("Transaction Type not found.");
//        }
//
//        return transactionType;
//    }
//}
//
