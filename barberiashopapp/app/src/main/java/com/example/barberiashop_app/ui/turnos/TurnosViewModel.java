package com.example.barberiashop_app.ui.turnos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.repository.TurnoRepository;
import com.example.barberiashop_app.domain.entity.Turno;

import java.util.List;

public class TurnosViewModel extends AndroidViewModel {
    private final TurnoRepository turnoRepository;
    private final LiveData<List<Turno>> turnosUsuario;

    public TurnosViewModel(@NonNull Application application) {
        super(application);

        turnoRepository = new TurnoRepository(application);

        // Obtener email del usuario logueado
        UserPreferences prefs = new UserPreferences(application);
        String email = prefs.getRegisteredEmail();

        // Obtener turnos del usuario
        turnosUsuario = turnoRepository.getTurnosByUsuario(email);
    }

    public LiveData<List<Turno>> getTurnosUsuario() {
        return turnosUsuario;
    }

    public void actualizarTurno(Turno turno) {
        turnoRepository.update(turno);
    }
}