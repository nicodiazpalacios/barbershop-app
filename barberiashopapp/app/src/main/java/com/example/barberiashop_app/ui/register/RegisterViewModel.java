package com.example.barberiashop_app.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.barberiashop_app.data.repository.UsuarioRepository;

public class RegisterViewModel extends AndroidViewModel {

    private final UsuarioRepository usuarioRepository;
    private MutableLiveData<Boolean> registrationResult = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        usuarioRepository = new UsuarioRepository(application);
    }

    public LiveData<Boolean> getRegistrationResult() {
        return registrationResult;
    }

    public void registerUser(String nombreCompleto, String email, String password, String celular, String photoURI) {
        String firstName = "";
        String lastName = "";

        if (nombreCompleto.contains(" ")) {
            String[] parts = nombreCompleto.split(" ", 2); // Divide en el primer espacio
            firstName = capitalize(parts[0]);
            lastName = capitalize(parts[1]);
        } else {
            firstName = capitalize(nombreCompleto);
            lastName = ".";
        }
        usuarioRepository.registerRemoto(firstName, lastName, email, password, registrationResult);
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
