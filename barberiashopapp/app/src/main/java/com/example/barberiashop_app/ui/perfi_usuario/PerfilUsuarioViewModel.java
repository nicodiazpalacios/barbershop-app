package com.example.barberiashop_app.ui.perfi_usuario;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.barberiashop_app.data.repository.UsuarioRepository;
import com.example.barberiashop_app.domain.entity.Usuario;

public class PerfilUsuarioViewModel extends AndroidViewModel {

    private final UsuarioRepository usuarioRepository;
    private final MutableLiveData<Usuario> userData = new MutableLiveData<>();
    private MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateResult = new MutableLiveData<>();

    public PerfilUsuarioViewModel(@NonNull Application application) {
        super(application);
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

    public void resetUpdateState() {
        updateResult.setValue(null);
    }

    public void logoutUser() {
        usuarioRepository.logout();
        logoutResult.setValue(true);
    }

    public void loadUserProfile() {
        usuarioRepository.fetchUserProfile(userData);
    }

    public void updateProfile(Usuario usuario) {
        String nombreCompleto = usuario.getNombre();
        String firstName = nombreCompleto;
        String lastName = "."; // Valor por defecto para apellido si no existe

        if (nombreCompleto.contains(" ")) {
            String[] parts = nombreCompleto.split(" ", 2);
            firstName = parts[0];
            lastName = parts[1];
        }

        usuarioRepository.updateProfileRemoto(
                firstName,
                lastName,
                usuario.getEmail(),
                usuario.getContrasenia(),
                updateResult
        );
    }

}
