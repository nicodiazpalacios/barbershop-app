package com.example.barberiashop_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barberiashop_app.domain.entity.Turno;

import java.util.List;

@Dao
public interface TurnoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Turno turno);

    @Update
    void update(Turno turno);

    @Delete
    void delete(Turno turno);
    @Query("DELETE FROM turnos")
    void deleteAll();

    @Query("SELECT * FROM turnos ORDER BY fecha ASC")
    LiveData<List<Turno>> getAllTurnos();

    @Query("SELECT * FROM turnos WHERE id = :id")
    Turno getTurnoById(int id);

    //Nuevo: obtener solo los turnos de un usuario específico
    @Query("SELECT * FROM turnos WHERE usuario_email = :email ORDER BY fecha ASC")
    LiveData<List<Turno>> getTurnosByUsuario(String email);

    // Consulta para verificar la existencia de un turno en un día y hora específicos
    @Query("SELECT COUNT(id) FROM turnos WHERE fecha = :fecha AND horario_inicio = :horario")
    int countTurnosByFechaAndHorario(String fecha, String horario);
}
