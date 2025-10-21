package com.example.barberiashop_app.domain.entity;


import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "turnos",
        foreignKeys = @ForeignKey(
                entity = EstadoTurno.class,
                parentColumns = "id",
                childColumns = "estado_id",
                onDelete = CASCADE
        )
)
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
    @ColumnInfo(name = "estado_id")
    private int estadoId;

    public Turno(@NonNull String fecha, @NonNull String horarioInicio,@NonNull String horarioFin, @NonNull String usuarioEmail) {
        this.fecha = fecha;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.usuarioEmail = usuarioEmail;
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
    public int getEstadoId() { return estadoId; }
    public void setEstadoId(int estadoId) { this.estadoId = estadoId; }

    // Método auxiliar para mostrar el nombre del estado
    public String getEstadoNombre() {
        switch (estadoId) {
            case 1:
                return "Pendiente";
            case 2:
                return "Confirmado";
            case 3:
                return "Cancelado";
            default:
                return "Desconocido";
        }
    }
}