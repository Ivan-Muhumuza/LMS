//package org.example.lms.service;
//
//import org.example.lms.model.Librarian;
//import org.example.lms.repository.LibrarianRepository;
//
//public class LibrarianService {
//
//    private final LibrarianRepository librarianRepository;
//
//    public LibrarianService(LibrarianRepository librarianRepository) {
//        this.librarianRepository = librarianRepository;
//    }
//
//    public void addLibrarian(Librarian librarian) throws IllegalArgumentException {
//        validateLibrarian(librarian);
//        librarianRepository.addLibrarianToDatabase(librarian);
//    }
//
//    public void updateLibrarian(Librarian librarian) throws IllegalArgumentException {
//        validateLibrarian(librarian);
//        librarianRepository.updateLibrarianInDatabase(librarian);
//    }
//
//    public void deleteLibrarian(int librarianID) {
//        if (librarianID <= 0) {
//            throw new IllegalArgumentException("Invalid librarian ID");
//        }
//        librarianRepository.deleteLibrarianFromDatabase(librarianID);
//    }
//
//    public Librarian getLibrarian(int librarianID) {
//        if (librarianID <= 0) {
//            throw new IllegalArgumentException("Invalid librarian ID");
//        }
//        return librarianRepository.getLibrarianFromDatabase(librarianID);
//    }
//
//    private void validateLibrarian(Librarian librarian) {
//        if (librarian == null) {
//            throw new IllegalArgumentException("Librarian cannot be null");
//        }
//        if (librarian.getLibrarianID() <= 0) {
//            throw new IllegalArgumentException("Invalid librarian ID");
//        }
//        if (librarian.getLibraryID() <= 0) {
//            throw new IllegalArgumentException("Invalid library ID");
//        }
//        if (librarian.getName() == null || librarian.getName().isEmpty()) {
//            throw new IllegalArgumentException("Librarian name cannot be empty");
//        }
//        if (librarian.getEmail() == null || librarian.getEmail().isEmpty()) {
//            throw new IllegalArgumentException("Librarian email cannot be empty");
//        }
//        // Additional email format validation can be added here
//    }
//}
//
