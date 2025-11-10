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
    private final LiveData<Usuario> userData;
    private MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateResult = new MutableLiveData<>();
   // private final MutableLiveData<UserPreferences.UserData> userData;

    public PerfilUsuarioViewModel(@NonNull Application application) {
        super(application);
//        userPrefs = new UserPreferences(application.getApplicationContext());
//        userData = new MutableLiveData<>(userPrefs.getUserData());
        usuarioRepository = new UsuarioRepository(application);
        // Obtiene LiveData<Usuario> del Repositorio
        userData = usuarioRepository.getLoggedInUser();
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

    public void logoutUser() {
        usuarioRepository.logout();
        logoutResult.setValue(true);
    }

    /**
     * Actualiza los datos del usuario logueado en la base de datos.
     * @param nuevoUsuario El objeto Usuario con los datos modificados.
     */
    public void updateProfile(Usuario nuevoUsuario) {
        // Ejecutamos la actualización y, al completarse, notificamos el resultado
        usuarioRepository.update(nuevoUsuario);

        // Asumimos éxito inmediato, Room se encargará de actualizar el LiveData automáticamente
        // si la operación fue exitosa.
        updateResult.setValue(true);
    }
}
