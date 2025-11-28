package com.example.barberiashop_app.ui.perfi_usuario;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.repository.UsuarioRepository;
import com.example.barberiashop_app.domain.entity.Usuario;

public class PerfilUsuarioViewModel extends AndroidViewModel {

    //private UserPreferences userPrefs;
    private final UsuarioRepository usuarioRepository;
    private final MutableLiveData<Usuario> userData = new MutableLiveData<>();
    private MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateResult = new MutableLiveData<>();
    // private final MutableLiveData<UserPreferences.UserData> userData;

    public PerfilUsuarioViewModel(@NonNull Application application) {
        super(application);
//        userPrefs = new UserPreferences(application.getApplicationContext());
//        userData = new MutableLiveData<>(userPrefs.getUserData());
        usuarioRepository = new UsuarioRepository(application);

        loadUserProfile();
    }

    public LiveData<Usuario> getUserData() {
        return userData;
    }

    public LiveData<Boolean> getLogoutResult() {
        return logoutResult;
    }

    public LiveData<Boolean> getUpdateResult() {
        return updateResult;
    }

    // --- NUEVO MÉTODO PARA RESETEAR EL ESTADO ---
    public void resetUpdateState() {
        updateResult.setValue(null);
    }

    public void logoutUser() {
        usuarioRepository.logout();
        logoutResult.setValue(true);
    }
    // --- CARGAR DESDE API ---
    public void loadUserProfile() {
        // Pedimos al repositorio que busque los datos frescos de la API
        usuarioRepository.fetchUserProfile(userData);
    }

    // --- ACTUALIZAR EN API ---
    public void updateProfile(Usuario usuario) {
        String nombreCompleto = usuario.getNombre();
        String firstName = nombreCompleto;
        String lastName = "."; // Valor por defecto para apellido si no existe

        if (nombreCompleto.contains(" ")) {
            String[] parts = nombreCompleto.split(" ", 2);
            firstName = parts[0];
            lastName = parts[1];
        }

        // Llamada al repositorio para el PUT
        usuarioRepository.updateProfileRemoto(
                firstName,
                lastName,
                usuario.getEmail(),
                usuario.getContrasenia(),
                updateResult
        );
    }

}
