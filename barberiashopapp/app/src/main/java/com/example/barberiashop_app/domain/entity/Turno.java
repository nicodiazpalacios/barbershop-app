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

    public Turno(@NonNull String fecha, @NonNull String horarioInicio,@NonNull String horarioFin) {
        this.fecha = fecha;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }

    public int getId() {
        return id;
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
}