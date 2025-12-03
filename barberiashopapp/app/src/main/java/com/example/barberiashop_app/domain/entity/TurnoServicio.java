package com.example.barberiashop_app.domain.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "turno_servicio", primaryKeys = { "id_turno", "id_servicio" }, // clave compuesta
        foreignKeys = {
                @ForeignKey(entity = Turno.class, parentColumns = "id", childColumns = "id_turno", onDelete = CASCADE),
                @ForeignKey(entity = Servicio.class, parentColumns = "id", childColumns = "id_servicio", onDelete = CASCADE)
        }, indices = { @Index("id_servicio") })
public class TurnoServicio {
    @ColumnInfo(name = "id_turno")
    @NonNull
    public int idTurno;

    @ColumnInfo(name = "id_servicio")
    @NonNull
    public int idServicio;

    public TurnoServicio(@NonNull int idTurno, @NonNull int idServicio) {
        this.idTurno = idTurno;
        this.idServicio = idServicio;
    }

    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }
}
