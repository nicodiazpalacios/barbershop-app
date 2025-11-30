package com.example.barberiashop_app.domain.entity;

import com.google.gson.annotations.SerializedName;

// Esta clase mapea la respuesta del Login y GetUser
public class UserResponse {
    @SerializedName("id")
    private String id;

    @SerializedName(value = "firstName", alternate = { "first_name", "nombre" })
    private String firstName;

    @SerializedName(value = "lastName", alternate = { "last_name", "apellido" })
    private String lastName;

    @SerializedName("email")
    private String email;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}