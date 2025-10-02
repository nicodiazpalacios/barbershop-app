package com.example.barberiashop_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barberiashop_app.domain.entity.Servicio;

import java.util.List;

@Dao
public interface ServicioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Servicio servicio);

    @Update
    void update(Servicio servicio);

    @Delete
    void delete(Servicio servicio);

    @Query("DELETE FROM servicios")
    void deleteAll();

    @Query("SELECT * FROM servicios ORDER BY nombre")
    LiveData<List<Servicio>> getAllServicios();

    @Query("SELECT * FROM servicios WHERE id = :id")
    Servicio getServicioById(int id);

}
