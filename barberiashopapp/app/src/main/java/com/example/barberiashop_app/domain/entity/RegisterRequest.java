package com.example.barberiashop_app.domain.entity;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    public RegisterRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
