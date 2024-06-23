package views;

public class Librarian {
    private int LibrarianID;
    private int LibraryID;
    private String Name;
    private String Email;

    public Librarian(int librarianID, int libraryID, String name, String email) {
        LibrarianID = librarianID;
        LibraryID = libraryID;
        Name = name;
        Email = email;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
