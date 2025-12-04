package com.example.barberiashop_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barberiashop_app.domain.entity.Notificacion;

import java.util.List;

@Dao
public interface NotificacionDao {
    @Insert
    void insert(Notificacion notificacion);

    @Update
    void update(Notificacion notificacion);

    @Delete
    void delete(Notificacion notificacion);

    @Query("SELECT * FROM notificaciones WHERE usuario_email = :email ORDER BY fecha DESC LIMIT 8")
    LiveData<List<Notificacion>> getAllByUsuario(String email);

    @Query("UPDATE notificaciones SET leido = 1 WHERE usuario_email = :email")
    void markAllAsRead(String email);

    @Query("SELECT COUNT(*) FROM notificaciones WHERE usuario_email = :email AND leido = 0")
    LiveData<Integer> getUnreadCount(String email);
}
