package com.example.barberiashop_app.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.data.repository.UsuarioRepository;
import com.example.barberiashop_app.domain.entity.Usuario;

public class RegisterViewModel extends AndroidViewModel {

    //    private UserPreferences userPrefs;
    private final UsuarioRepository usuarioRepository;
    private MutableLiveData<Boolean> registrationResult = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
//        userPrefs = new UserPreferences(application.getApplicationContext());
        usuarioRepository = new UsuarioRepository(application);
    }

    public LiveData<Boolean> getRegistrationResult() {
        return registrationResult;
    }

    //    public void registerUser(String name, String email, String password, String celular, String photoURI) {
//        Usuario nuevoUsuario = new Usuario(name, email, password, photoURI, celular, 1); // 1 = rol cliente
//
//        // 1. Insertar en Room a través del Repositorio (asíncrono)
//        usuarioRepository.register( nuevoUsuario);
//
//        // 2. Guardar estado de sesión (asumiendo que el registro implica login)
//        usuarioRepository.setLoggedIn(true);
//        usuarioRepository.saveLoggedInUserEmail(email); // Guardar el email en SharedPreferences
//
//        // 3. Notificar a la UI
//        registrationResult.postValue(true);
//    }
    public void registerUser(String nombreCompleto, String email, String password, String celular, String photoURI) {

        // --- CORRECCIÓN PASO 2: Formato de Datos ---

        String firstName = "";
        String lastName = "";

        // 1. Separar Nombre y Apellido (si el usuario puso espacio)
        if (nombreCompleto.contains(" ")) {
            String[] parts = nombreCompleto.split(" ", 2); // Divide en el primer espacio
            firstName = capitalize(parts[0]);
            lastName = capitalize(parts[1]);
        } else {
            firstName = capitalize(nombreCompleto);
            // El backend pide apellido obligatoriamente. Si no ponen, ponemos un punto o repetimos.
            lastName = ".";
        }

        // Llamada remota pasando los datos ya formateados
        usuarioRepository.registerRemoto(firstName, lastName, email, password, registrationResult);
    }

    // Método auxiliar para poner la primera letra en mayúscula (Requisito estricto del Backend)
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // Convierte primera a mayúscula y el resto a minúscula
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
