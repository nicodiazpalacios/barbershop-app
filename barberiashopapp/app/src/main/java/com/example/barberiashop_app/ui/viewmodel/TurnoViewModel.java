package com.example.barberiashop_app.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.barberiashop_app.data.repository.TurnoRepository;
import com.example.barberiashop_app.domain.entity.Turno;

import java.util.List;

public class TurnoViewModel extends AndroidViewModel  {
    private final TurnoRepository turnoRepository;
    private final LiveData<List<Turno>> allTurnos;
    public TurnoViewModel(@NonNull Application application) {
        super(application);
        turnoRepository = new TurnoRepository(application);
        allTurnos = turnoRepository.getAllTurnos();
    }
    public LiveData<List<Turno>> getAllTurnos() {return allTurnos;}
    public void insert(Turno turno) {turnoRepository.insert(turno);}

    public void update(Turno turno) {turnoRepository.update(turno);}
    public void delete(Turno turno) {turnoRepository.delete(turno);}
    public void deleteAll() {turnoRepository.deleteAll();}
}
