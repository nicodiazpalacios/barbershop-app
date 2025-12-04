package com.example.barberiashop_app.domain.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "turnos", foreignKeys = @ForeignKey(entity = EstadoTurno.class, parentColumns = "id", childColumns = "estado_id", onDelete = CASCADE), indices = {
        @Index("estado_id") })
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
    private String usuarioEmail;
    @ColumnInfo(name = "estado_id")
    private int estadoId;
    @ColumnInfo(name = "peluquero")
    private String peluquero;

    public Turno() {
    }

    @Ignore
    public Turno(@NonNull String fecha, @NonNull String horarioInicio, @NonNull String horarioFin,
            @NonNull String usuarioEmail, String peluquero) {
        this.fecha = fecha;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.usuarioEmail = usuarioEmail;
        this.peluquero = peluquero;
        this.estadoId = 1; // Por defecto: pendiente
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

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(String horarioFin) {
        this.horarioFin = horarioFin;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getPeluquero() {
        return peluquero;
    }

    public void setPeluquero(String peluquero) {
        this.peluquero = peluquero;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    // Método auxiliar para mostrar el nombre del estado
    public String getEstadoNombre() {
        switch (estadoId) {
            case 1:
                return "Pendiente";
            case 2:
                return "Confirmado";
            case 3:
                return "Cancelado";
            case 4:
                return "Finalizado";
            default:
                return "Desconocido";
        }
    }
}