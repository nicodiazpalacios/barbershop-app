
package com.example.barberiashop_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barberiashop_app.domain.entity.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    // Inserta un nuevo usuario (registro) o lo reemplaza si hay conflicto de PK/UNIQUE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Query("SELECT * FROM usuario")
    LiveData<List<Usuario>> getAll();

    // Obtiene un usuario para el login/validación
    @Query("SELECT * FROM usuario WHERE email = :email AND contrasenia = :password LIMIT 1")
    Usuario getUserByCredentials(String email, String password); // No es LiveData, se llama en hilo de background

    // Obtiene el usuario por email (útil para cargar el perfil)
    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    LiveData<Usuario> getUserByEmail(String email);

    // Obtener un usuario sincrónico por email (útil para el Repository)
    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    Usuario getUserByEmailSync(String email);
    @Query("SELECT * FROM usuario WHERE id = :id")
    LiveData<Usuario> findById(int id);

    @Query("SELECT * FROM usuario WHERE email = :email")
    LiveData<Usuario> findByEmail(String email);

    @Query("SELECT * FROM usuario WHERE nombre = :nombre LIMIT 1")
    LiveData<Usuario> getUsuarioPorNombre(String nombre );

    //Esta devuelve directamente el objeto Usuario (no LiveData), ideal para lógica interna.
    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    Usuario getUsuarioPorEmailSync(String email);

    @Query("SELECT * FROM usuario WHERE email = :email AND contrasenia = :password LIMIT 1")
    Usuario login(String email, String password);

}
