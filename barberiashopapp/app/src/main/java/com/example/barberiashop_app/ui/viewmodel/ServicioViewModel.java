package com.example.barberiashop_app.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.barberiashop_app.data.repository.ServicioRepository;
import com.example.barberiashop_app.domain.entity.Servicio;

import java.util.List;

public class ServicioViewModel  extends AndroidViewModel {
    private final ServicioRepository servicioRepository;
    private final LiveData<List<Servicio>> allServicios;
    public ServicioViewModel(@NonNull Application application) {
        super(application);
        servicioRepository = new ServicioRepository(application);
        allServicios = servicioRepository.getAllServicios();
    }

    //getAll
    //insert
    //update
    //delete
    //deleteAll
}
