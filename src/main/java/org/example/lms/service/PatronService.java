package org.example.lms.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Book;
import org.example.lms.model.Patron;
import org.example.lms.repository.PatronRepository;

import java.sql.SQLException;
import java.util.List;

public class PatronService {

    private final PatronRepository patronRepository;

    public PatronService() {
        this.patronRepository = new PatronRepository();
    }

    public void addPatron(Patron patron) {
        patronRepository.addPatron(patron);
    }

    public void updatePatron(Patron patron) {
        patronRepository.updatePatron(patron);
    }

    public void deletePatron(long patronID) {
        patronRepository.deletePatron(patronID);
    }

    public ObservableList<Patron> getAllPatrons() {
        List<Patron> patrons = patronRepository.getAllPatrons();
        return FXCollections.observableArrayList(patrons);
    }
}

