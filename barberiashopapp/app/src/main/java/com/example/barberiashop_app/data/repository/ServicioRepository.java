package com.example.barberiashop_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.barberiashop_app.data.dao.ServicioDao;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.domain.entity.Servicio;

import java.util.List;

//TODO: Controlar las excepciones para debugg
public class ServicioRepository {
    private ServicioDao servicioDao;
    private LiveData<List<Servicio>> allServicios;

    public ServicioRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        servicioDao = appDatabase.servicioDao();
        allServicios = servicioDao.getAllServicios();
    }

    public LiveData<List<Servicio>> getAllServicios() {
        return allServicios;
    }

    public LiveData<Servicio> getServicioById(int id) {
        return servicioDao.getServicioById(id);
    }

    public void insert(Servicio servicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> servicioDao.insert(servicio));
    }

    public void update(Servicio servicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> servicioDao.update(servicio));
    }

    public void delete(Servicio servicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> servicioDao.delete(servicio));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> servicioDao.deleteAll());
    }

    public int count() {
        // Nota: Esto debería ejecutarse en un hilo secundario si se llama desde la UI,
        // pero para una verificación rápida al inicio puede ser aceptable o manejarse
        // con Future.
        // Por simplicidad y dado que es un count rápido:
        try {
            return AppDatabase.databaseWriteExecutor.submit(() -> servicioDao.count()).get();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
