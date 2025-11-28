package com.example.barberiashop_app.domain.entity;

// Esta clase mapea la respuesta del Login y GetUser
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;


    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
}