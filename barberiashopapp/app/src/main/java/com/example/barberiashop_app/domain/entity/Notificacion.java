package com.example.barberiashop_app.domain.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notificaciones")
public class Notificacion {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "usuario_email")
    @NonNull
    private String usuarioEmail;

    @NonNull
    private String titulo;

    @NonNull
    private String mensaje;

    private long fecha; // Timestamp

    private boolean leido;

    @androidx.room.Ignore
    public Notificacion(@NonNull String usuarioEmail, @NonNull String titulo, @NonNull String mensaje, long fecha) {
        this.usuarioEmail = usuarioEmail;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leido = false;
    }

    @androidx.room.Ignore
    public Notificacion(int id, @NonNull String usuarioEmail, @NonNull String titulo, @NonNull String mensaje,
            long fecha, boolean leido) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leido = leido;
    }

    public Notificacion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(@NonNull String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NonNull String titulo) {
        this.titulo = titulo;
    }

    @NonNull
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(@NonNull String mensaje) {
        this.mensaje = mensaje;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}
