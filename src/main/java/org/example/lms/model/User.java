package org.example.lms.model;

public class User {
    private String Name;
    private String Email;
    private String Password;

    public User(String name, String email, String password) {
        this.Name = name;
        this.Email = email;
        this.Password = password;
    }

    // Getters and Setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}

