package org.example.lms.service;

import org.example.lms.model.Patron;
import org.example.lms.repository.PatronRepository;

import java.sql.SQLException;

public class PatronService {

    private final PatronRepository patronRepository;

    public PatronService() throws SQLException {
        this.patronRepository = new PatronRepository();
    }

    public void addPatron(Patron patron) throws SQLException {
        validatePatron(patron);
        if (patronExists(patron.getPatronID())) {
            throw new IllegalArgumentException("Patron with this ID already exists.");
        }
        patronRepository.addPatronToDatabase(patron);
    }

    public void updatePatron(Patron patron) throws SQLException {
        validatePatron(patron);
        if (!patronExists(patron.getPatronID())) {
            throw new IllegalArgumentException("Patron not found for update.");
        }
        patronRepository.updatePatronInDatabase(patron);
    }

    public void deletePatron(int patronID) throws SQLException {
        if (!patronExists(patronID)) {
            throw new IllegalArgumentException("Patron not found for deletion.");
        }
        patronRepository.deletePatronFromDatabase(patronID);
    }

    public Patron getPatron(int patronID) throws SQLException {
        Patron patron = patronRepository.getPatronFromDatabase(patronID);
        if (patron == null) {
            throw new IllegalArgumentException("Patron not found.");
        }
        return patron;
    }

    private void validatePatron(Patron patron) {
        if (patron.getPatronID() <= 0) {
            throw new IllegalArgumentException("Patron ID must be positive.");
        }
        if (patron.getName() == null || patron.getName().isEmpty()) {
            throw new IllegalArgumentException("Patron name cannot be null or empty.");
        }
        if (patron.getEmail() == null || patron.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Patron email cannot be null or empty.");
        }
        // Additional email format validation can be added here
    }

    private boolean patronExists(int patronID) throws SQLException {
        Patron patron = patronRepository.getPatronFromDatabase(patronID);
        return patron != null;
    }
}

