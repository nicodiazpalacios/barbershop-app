
package com.example.barberiashop_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barberiashop_app.domain.entity.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void insert(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Query("SELECT * FROM usuario")
    LiveData<List<Usuario>> getAll();

    @Query("SELECT * FROM usuario WHERE id = :id")
    LiveData<Usuario> findById(int id);

    @Query("SELECT * FROM usuario WHERE email = :email")
    LiveData<Usuario> findByEmail(String email);
}
