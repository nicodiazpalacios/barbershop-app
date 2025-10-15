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

     //condicional para hacer populate
//     if(allServicios.getValue() == null | allServicios.getValue().isEmpty()){
//         insertSampleData();
//     }
    }

    public LiveData<List<Servicio>> getAllServicios(){
        return allServicios;
    }

    //metodo para hacer populate
//    private void insertSampleData() {
//        servicioRepository.insert(new Servicio("Corte de pelo", "Descripción apropiada para un corte de pelo", 5.99, 30));
//        servicioRepository.insert(new Servicio("Arreglo de barba", "Afeitado clásico y perfilado de barba", 8.50, 45));
//        servicioRepository.insert(new Servicio("Tratamiento facial", "Limpieza profunda y masaje relajante", 15.00, 60));
//    }



}