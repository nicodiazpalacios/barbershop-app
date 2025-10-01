package com.example.barberiashop_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.barberiashop_app.data.dao.TurnoDao;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.domain.entity.Turno;

import java.util.List;

public class TurnoRepository {

    private TurnoDao turnoDao;
    private LiveData<List<Turno>> allTurnos;

    public TurnoRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        turnoDao = appDatabase.turnoDao();
        allTurnos = turnoDao.getAllTurnos();
    }

    public LiveData<List<Turno>> getAllTurnos() {
        return allTurnos;
    }

    public void insert(Turno turno){
        AppDatabase.databaseWriteExecutor.execute(() -> turnoDao.insert(turno));
    }

    public void update(Turno turno){
        AppDatabase.databaseWriteExecutor.execute(() -> turnoDao.update(turno));
    }

    public void delete(Turno turno){
        AppDatabase.databaseWriteExecutor.execute(() -> turnoDao.delete(turno));
    }
    public void deleteAll(){
        AppDatabase.databaseWriteExecutor.execute(() -> turnoDao.deleteAll());
    }

}
