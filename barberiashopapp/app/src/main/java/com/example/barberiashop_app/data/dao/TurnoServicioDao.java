
package com.example.barberiashop_app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.barberiashop_app.domain.entity.TurnoServicio;

import java.util.List;

@Dao
public interface TurnoServicioDao {

    @Insert
    void insert(TurnoServicio turnoServicio);

    @Query("SELECT * FROM turno_servicio WHERE id_turno = :turnoId")
    List<TurnoServicio> findByTurnoId(int turnoId);

    @Query("SELECT * FROM turno_servicio WHERE id_servicio = :servicioId")
    List<TurnoServicio> findByServicioId(int servicioId);
}
