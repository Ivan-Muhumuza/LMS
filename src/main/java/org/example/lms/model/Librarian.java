package org.example.lms.model;

public class Librarian extends User{
    private int LibrarianID;
    private int LibraryID;

    public Librarian(int librarianID, int libraryID, String name, String email, String password) {
        super(name, email, password);
        this.LibrarianID = librarianID;
        this.LibraryID = libraryID;
    }


    public int getLibrarianID() {
        return LibrarianID;
    }

    public void setLibrarianID(int librarianID) {
        LibrarianID = librarianID;
    }

    public int getLibraryID() {
        return LibraryID;
    }

    public void setLibraryID(int libraryID) {
        LibraryID = libraryID;
    }
}
