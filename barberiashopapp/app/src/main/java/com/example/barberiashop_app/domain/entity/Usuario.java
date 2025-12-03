package com.example.barberiashop_app.domain.entity;

import static androidx.room.ForeignKey.NO_ACTION;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuario", foreignKeys = @ForeignKey(entity = Rol.class, parentColumns = "id", childColumns = "rol_id", onDelete = NO_ACTION),
        // (uniques constraints) de SQL
        // (uniques constraints) de SQL
        indices = {
                @Index(value = { "email" }, unique = true),
                @Index("rol_id")
        })
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String nombre;

    @NonNull
    private String email;

    @NonNull
    private String contrasenia; // almacenada como hash, no texto plano

    @ColumnInfo(name = "foto_url")
    private String fotoUrl;

    @NonNull
    private String celular;

    @ColumnInfo(name = "rol_id")
    private int rolId; // clave foranea a rol

    public Usuario(@NonNull String nombre, @NonNull String email, @NonNull String contrasenia, String fotoUrl,
            @NonNull String celular, int rolId) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.fotoUrl = fotoUrl;
        this.celular = celular;
        this.rolId = rolId;
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

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(@NonNull String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    @NonNull
    public String getCelular() {
        return celular;
    }

    public void setCelular(@NonNull String celular) {
        this.celular = celular;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

}
