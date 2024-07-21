package org.example.lms.model;


import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patron {
    private final LongProperty patronID;
    private final StringProperty name;
    private final StringProperty email;

    public Patron(long patronID, String name, String email) {
        this.patronID = new SimpleLongProperty(patronID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
    }

    // Getters and setters
    public long getPatronID() { return patronID.get(); }
    public void setPatronID(long patronID) { this.patronID.set(patronID); }
    public LongProperty patronIDProperty() { return patronID; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }
}
