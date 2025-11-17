package com.example.barberiashop_app.ui.reservar_turno;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.barberiashop_app.data.repository.ServicioRepository;
import com.example.barberiashop_app.data.repository.TurnoRepository;
import com.example.barberiashop_app.domain.entity.Servicio;
import com.example.barberiashop_app.domain.entity.Turno;

import java.util.concurrent.ExecutionException;

public class ReservarTurnoViewModel extends AndroidViewModel {
    private final ServicioRepository servicioRepository;
    private final TurnoRepository turnoRepository;

    public ReservarTurnoViewModel(@NonNull Application application) {
        super(application);
        servicioRepository = new ServicioRepository(application);
        turnoRepository = new TurnoRepository(application);
    }

    public LiveData<Servicio> getServicioById(int id) {
        return servicioRepository.getServicioById(id);
    }

    public void insertTurno(Turno turno) {
        turnoRepository.insert(turno);
    }

    public int countTurnosByFechaAndHorario(String fecha, String horario) throws ExecutionException, InterruptedException {
        return turnoRepository.countTurnosByFechaAndHorario(fecha, horario);
    }
}


