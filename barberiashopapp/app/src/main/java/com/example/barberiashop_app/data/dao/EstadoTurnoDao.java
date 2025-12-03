
package com.example.barberiashop_app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barberiashop_app.domain.entity.EstadoTurno;

import java.util.List;

@Dao
public interface EstadoTurnoDao {

    @Insert
    void insert(EstadoTurno estadoTurno);

    @Update
    void update(EstadoTurno estadoTurno);

    @Query("SELECT * FROM estado_turno")
    List<EstadoTurno> getAll();

    @Query("SELECT * FROM estado_turno WHERE id = :id")
    EstadoTurno findById(int id);

    @Query("SELECT COUNT(*) FROM estado_turno")
    int count();

    @Query("SELECT * FROM estado_turno WHERE nombre = :nombre LIMIT 1")
    EstadoTurno findByName(String nombre);
}
