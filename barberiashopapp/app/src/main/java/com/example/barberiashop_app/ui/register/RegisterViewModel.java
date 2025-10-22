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

    private UserPreferences userPrefs;
    private MutableLiveData<Boolean> registrationResult = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        userPrefs = new UserPreferences(application.getApplicationContext());
    }

    public LiveData<Boolean> getRegistrationResult() {
        return registrationResult;
    }

    public void registerUser(String name, String email, String password, String celular, String photoURI) {
        Usuario nuevoUsuario = new Usuario(name, email, password, photoURI, celular, 1); // 1 = rol cliente

        // Guardar en Room
        AppDatabase.databaseWriteExecutor.execute(() -> {
            UsuarioRepository repo = new UsuarioRepository(getApplication());
            repo.insert(nuevoUsuario);
        });

        // Guardar en SharedPreferences
        userPrefs.registerUser(name, email, celular, password, photoURI);
        userPrefs.setLoggedIn(true);
        registrationResult.postValue(true);
    }
}
