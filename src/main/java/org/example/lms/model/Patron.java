package org.example.lms.model;

public class Patron extends User {
    private Integer PatronID;

    public Patron(int patronID, String name, String email, String password) {
        super(name, email, password);
        this.PatronID = patronID;
    }

    public Integer getPatronID() {
        return PatronID;
    }

    public void setPatronID(int patronID) {
        this.PatronID = patronID;
    }

}
