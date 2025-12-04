package com.example.barberiashop_app.ui.servicios;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.barberiashop_app.data.repository.ServicioRepository;
import com.example.barberiashop_app.domain.entity.Servicio;

import java.util.List;

public class ServiciosViewModel extends AndroidViewModel {

    private final ServicioRepository servicioRepository;
    private final LiveData<List<Servicio>> allServicios;

    public ServiciosViewModel(@NonNull Application application) {
        super(application);
        servicioRepository = new ServicioRepository(application);
        allServicios = servicioRepository.getAllServicios();

        // Verificar si hay datos en background para no bloquear la UI
        new Thread(() -> {
            if (servicioRepository.count() == 0) {
                insertSampleData();
            }
        }).start();
    }

    public LiveData<List<Servicio>> getAllServicios() {
        return allServicios;
    }

    // metodo para hacer populate
    private void insertSampleData() {
        servicioRepository.insert(
                new Servicio("Corte de pelo (Hombre)", "Corte clásico o moderno con tijera y/o máquina.", 8.00, 40));
        servicioRepository.insert(
                new Servicio("Arreglo de barba", "Afeitado tradicional con navaja y perfilado de barba.", 7.50, 30));
        servicioRepository.insert(new Servicio("Corte y Barba Full",
                "Servicio completo de corte de pelo y arreglo de barba.", 14.50, 70));
        servicioRepository
                .insert(new Servicio("Diseño de cejas", "Perfilado profesional de cejas con cera o pinzas.", 4.00, 15));
        servicioRepository.insert(
                new Servicio("Lavado y Secado", "Lavado con productos especializados y peinado rápido.", 4.50, 20));
    }

}