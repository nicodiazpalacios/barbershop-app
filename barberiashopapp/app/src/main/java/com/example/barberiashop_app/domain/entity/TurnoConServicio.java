package com.example.barberiashop_app.domain.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;


public class TurnoConServicio {

    @Embedded
    public Turno turno;

    @Relation(
            // 1. Relación Turno -> TurnoServicio
            parentColumn = "id", // ID del Turno (desde la entidad Turno incrustada)

            // 2. Relación TurnoServicio -> Servicio
            entityColumn = "id", // ID del Servicio (desde la entidad Servicio)

            associateBy = @Junction(
                    value = TurnoServicio.class, // Tabla de unión
                    parentColumn = "id_turno",    // Campo de la tabla de unión que apunta al Turno
                    entityColumn = "id_servicio"  // Campo de la tabla de unión que apunta al Servicio
            ),
            entity = Servicio.class
    )
    public List<Servicio> servicios; // Un turno puede tener uno o más servicios
}