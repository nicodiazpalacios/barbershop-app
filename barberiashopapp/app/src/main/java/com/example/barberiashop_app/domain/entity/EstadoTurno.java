package com.example.barberiashop_app.domain.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estado_turno")
public class EstadoTurno {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String nombre; //confirmado, pendiente, cancelado

    public EstadoTurno(@NonNull String nombre){
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    @NonNull public String getNombre() { return nombre; }
    public void setNombre(@NonNull String nombre) { this.nombre = nombre; }
}
