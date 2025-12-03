package com.example.barberiashop_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.barberiashop_app.data.dao.TurnoDao;
import com.example.barberiashop_app.data.dao.TurnoServicioDao;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.domain.entity.Turno;
import com.example.barberiashop_app.domain.entity.TurnoConServicio;
import com.example.barberiashop_app.domain.entity.TurnoServicio;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.example.barberiashop_app.data.dao.EstadoTurnoDao;
import com.example.barberiashop_app.domain.entity.EstadoTurno;

public class TurnoRepository {

    private TurnoDao turnoDao;
    private final TurnoServicioDao turnoServicioDao;
    private final EstadoTurnoDao estadoTurnoDao;
    private LiveData<List<Turno>> allTurnos;

    public TurnoRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        turnoDao = appDatabase.turnoDao();
        turnoServicioDao = appDatabase.turnoServicioDao();
        estadoTurnoDao = appDatabase.estadoTurnoDao();
        allTurnos = turnoDao.getAllTurnos();
    }

    public LiveData<List<Turno>> getAllTurnos() {
        return allTurnos;
    }

    public LiveData<List<Turno>> getTurnosByUsuario(String email) {
        return turnoDao.getTurnosByUsuario(email);
    }

    // MÉTODO ANTIGUO: public void insert(Turno turno) { ... }
    // NUEVO MÉTODO: Insertar Turno y su relación, devolviendo el ID.
    public long insertTurnoAndServicio(Turno turno, int servicioId) throws ExecutionException, InterruptedException {
        Callable<Long> insertCallable = () -> {
            // 0. Asegurar que existe el estado "pendiente"
            EstadoTurno estadoPendiente = estadoTurnoDao.findByName("pendiente");
            if (estadoPendiente == null) {
                estadoTurnoDao.insert(new EstadoTurno("pendiente"));
                estadoPendiente = estadoTurnoDao.findByName("pendiente");
            }

            // Asignar el ID correcto del estado
            if (estadoPendiente != null) {
                turno.setEstadoId(estadoPendiente.getId());
            }

            // 1. Insertar Turno y obtener el ID
            long turnoId = turnoDao.insert(turno);

            // 2. Crear y Insertar la relación
            TurnoServicio ts = new TurnoServicio((int) turnoId, servicioId);
            turnoServicioDao.insert(ts);

            return turnoId;
        };

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        return future.get(); // Esperar el resultado síncronamente (en el background thread de Room)
    }

    public void update(Turno turno) {
        AppDatabase.databaseWriteExecutor.execute(() -> turnoDao.update(turno));
    }

    public void delete(Turno turno) {
        AppDatabase.databaseWriteExecutor.execute(() -> turnoDao.delete(turno));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> turnoDao.deleteAll());
    }

    // verificar la disponibilidad
    public int countTurnosByFechaAndHorario(String fecha, String horario)
            throws ExecutionException, InterruptedException {
        Future<Integer> future = AppDatabase.databaseWriteExecutor
                .submit(() -> turnoDao.countTurnosByFechaAndHorario(fecha, horario));
        return future.get(); // Espera y obtiene el resultado de la consulta
    }

    /**
     * Obtiene los turnos con la información de servicio
     * 
     * @param email
     * @return
     */
    public LiveData<List<TurnoConServicio>> getTurnosConServicioByUsuario(String email) {
        return turnoDao.getTurnosConServiciosByUsuario(email);
    }
}
