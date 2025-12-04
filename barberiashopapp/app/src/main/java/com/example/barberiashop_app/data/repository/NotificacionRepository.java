package com.example.barberiashop_app.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.barberiashop_app.data.dao.NotificacionDao;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.domain.entity.Notificacion;
import java.util.List;

public class NotificacionRepository {
    private NotificacionDao notificacionDao;

    public NotificacionRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        notificacionDao = db.notificacionDao();
    }

    public LiveData<List<Notificacion>> getAllByUsuario(String email) {
        return notificacionDao.getAllByUsuario(email);
    }

    public void insert(Notificacion notificacion) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificacionDao.insert(notificacion);
        });
    }

    // Método síncrono para usar en Workers (ya están en background)
    public void insertSync(Notificacion notificacion) {
        notificacionDao.insert(notificacion);
    }

    public void markAllAsRead(String email) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificacionDao.markAllAsRead(email);
        });
    }

    public LiveData<Integer> getUnreadCount(String email) {
        return notificacionDao.getUnreadCount(email);
    }
}
