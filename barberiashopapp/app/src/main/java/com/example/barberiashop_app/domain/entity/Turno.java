package com.example.barberiashop_app.domain.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "turnos")
public class Turno {

    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "fecha")
    private String fecha;
    @ColumnInfo(name = "horario_inicio")
    private String horarioInicio;
    @ColumnInfo(name = "horario_fin")
    private String horarioFin;
    @ColumnInfo(name = "usuario_email")
    private String usuarioEmail; //

    public Turno(@NonNull String fecha, @NonNull String horarioInicio,@NonNull String horarioFin, @NonNull String usuarioEmail) {
        this.fecha = fecha;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.usuarioEmail = usuarioEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public String getHorarioFin() {
        return horarioFin;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }
}