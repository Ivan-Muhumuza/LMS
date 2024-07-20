package org.example.lms.model;

public class Patron {
    private Integer PatronID;
    private String Name;
    private String Email;

    public Patron(Integer patronID, String name, String email) {
        this.PatronID = patronID;
        this.Name = name;
        this.Email = email;
    }

    public Integer getPatronID() {
        return PatronID;
    }

    public void setPatronID(Integer patronID) {
        this.PatronID = patronID;
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
