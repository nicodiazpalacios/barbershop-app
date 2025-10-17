
package com.example.barberiashop_app.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barberiashop_app.domain.entity.Rol;

import java.util.List;

@Dao
public interface RolDao {

    @Insert
    void insert(Rol rol);

    @Update
    void update(Rol rol);

    @Query("SELECT * FROM rol")
    List<Rol> getAll();

    @Query("SELECT * FROM rol WHERE id = :id")
    Rol findById(int id);
}
