package com.example.landscape.classes;

public class User {

    String Name;
    String Email;


    public User() {
        Name = "User";
        Email = "User@gmail.com";
    }

    public User(String name, String email) {
        Name = name;
        Email = email;
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
