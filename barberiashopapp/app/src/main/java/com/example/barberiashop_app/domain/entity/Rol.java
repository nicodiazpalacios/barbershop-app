package com.example.barberiashop_app.domain.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rol")
public class Rol {

    @PrimaryKey(autoGenerate = true)
    private int id;


    @NonNull
    private String nombre; //ej: cliente, barbero, propietario

    public Rol(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }
}
