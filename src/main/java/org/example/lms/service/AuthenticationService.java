package org.example.lms.service;


import org.example.lms.model.Librarian;
import org.example.lms.model.Patron;
import org.example.lms.repository.LibrarianRepository;
import org.example.lms.repository.PatronRepository;
import org.example.lms.util.DatabaseUtil;

public class AuthenticationService {
    private LibrarianRepository librarianRepository;
    private PatronRepository patronRepository;

    public AuthenticationService(LibrarianRepository librarianRepository, PatronRepository patronRepository) {
        this.librarianRepository = librarianRepository;
        this.patronRepository = patronRepository;
    }

    public Librarian authenticateLibrarian(String email, String password) {
        Librarian librarian = librarianRepository.getLibrarianByEmail(email);
        if (librarian != null && DatabaseUtil.checkPassword(password, librarian.getPassword())) {
            return librarian;
        }
        return null;
    }
}

